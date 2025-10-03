const WebSocket = require('ws');
const express = require('express');
const http = require('http');

// ===== CHANNEL MANAGER =====
class ChannelManager {
  constructor() {
    this.channels = new Map();
    this.clientMetadata = new Map();
    this.statusClients = new Set();
    this.statusClientChannels = new Map(); // Maps status client -> subscribed channel number
    this.channelTokens = new Map(); // Maps channel number -> token
  }

  // Generate unique client ID
  generateClientId() {
    return `client_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
  }

  // Generate secure channel token
  generateChannelToken() {
    const timestamp = Date.now().toString(36);
    const random1 = Math.random().toString(36).substring(2, 15);
    const random2 = Math.random().toString(36).substring(2, 15);
    return `token_${timestamp}_${random1}${random2}`;
  }

  // Validate channel token
  validateChannelToken(channel, token) {
    if (!token || typeof token !== 'string') {
      return false;
    }
    const storedToken = this.channelTokens.get(channel);
    return storedToken === token;
  }

  // Store token for channel
  storeChannelToken(channel, token) {
    this.channelTokens.set(channel, token);
    console.log(`Token stored for channel ${channel}`);
  }

  // Remove token for channel
  removeChannelToken(channel) {
    if (this.channelTokens.has(channel)) {
      this.channelTokens.delete(channel);
      console.log(`Token removed for channel ${channel}`);
    }
  }

  // Add client to channel
  addClient(ws, channel) {
    const clientId = this.generateClientId();
    const metadata = {
      id: clientId,
      channel,
      joinTime: new Date()
    };

    console.log(`Adding client ${clientId} to channel ${channel}`);

    // Store metadata
    this.clientMetadata.set(ws, metadata);
    ws.channel = channel;
    ws.clientId = clientId;

    // Add to channel
    if (!this.channels.has(channel)) {
      this.channels.set(channel, new Set());
      console.log(`Created new channel ${channel}`);
    }
    this.channels.get(channel).add(ws);
    
    const channelSize = this.channels.get(channel).size;
    console.log(`Channel ${channel} now has ${channelSize} clients`);

    // Notify status clients
    this.notifyStatusClients({
      type: 'client_joined',
      channel,
      clientId,
      timestamp: new Date(),
      channelActivity: this.getChannelActivity()
    });

    return { clientId, metadata };
  }

  // Remove client from channel
  removeClient(ws, reason = 'disconnected') {
    const metadata = this.clientMetadata.get(ws);
    if (!metadata) {
      console.log(`Attempted to remove client with no metadata (reason: ${reason})`);
      return;
    }

    const { channel, id: clientId } = metadata;
    const channelClients = this.channels.get(channel);
    
    console.log(`Removing client ${clientId} from channel ${channel} (reason: ${reason})`);
    
    if (channelClients) {
      const wasInChannel = channelClients.has(ws);
      channelClients.delete(ws);
      
      console.log(`Client was ${wasInChannel ? 'found' : 'NOT found'} in channel ${channel}`);
      
      if (channelClients.size === 0) {
        this.channels.delete(channel);
        this.removeChannelToken(channel); // Clean up token when channel becomes empty
        console.log(`Channel ${channel} deleted (empty)`);
      } else {
        console.log(`Channel ${channel} still has ${channelClients.size} clients`);
      }
    } else {
      console.log(`Channel ${channel} not found when removing client ${clientId}`);
    }

    // Notify status clients
    this.notifyStatusClients({
      type: reason === 'error' ? 'client_error' : 'client_left',
      channel,
      clientId,
      error: reason === 'error' ? 'WebSocket error' : undefined,
      timestamp: new Date(),
      channelActivity: this.getChannelActivity()
    });

    this.clientMetadata.delete(ws);
    console.log(`Client ${clientId} metadata removed`);
  }

  // Broadcast message to channel
  broadcastToChannel(channel, message) {
    const channelClients = this.channels.get(channel);
    let sentCount = 0;

    if (channelClients) {
      const messageStr = JSON.stringify(message);
      console.log(`Broadcasting to channel ${channel}, ${channelClients.size} potential recipients`);
      
      channelClients.forEach((client) => {
        if (client.readyState === WebSocket.OPEN) {
          client.send(messageStr);
          sentCount++;
        } else {
          console.log(`Removing dead client from channel ${channel}`);
          channelClients.delete(client);
        }
      });

      // Clean up empty channels
      if (channelClients.size === 0) {
        this.channels.delete(channel);
        this.removeChannelToken(channel); // Clean up token when channel becomes empty
        console.log(`Channel ${channel} deleted after cleanup`);
      }
      
      console.log(`Successfully sent to ${sentCount} clients in channel ${channel}`);
    } else {
      console.log(`No clients found in channel ${channel}`);
    }

    return sentCount;
  }

  // Get channel activity summary
  getChannelActivity() {
    const activity = {};
    this.channels.forEach((clients, channelId) => {
      const activeClients = Array.from(clients).filter(client => client.readyState === WebSocket.OPEN);
      activity[channelId] = {
        clientCount: activeClients.length,
        clients: activeClients.map(ws => {
          const metadata = this.clientMetadata.get(ws);
          return metadata ? {
            id: metadata.id,
            joinTime: metadata.joinTime,
          } : { id: 'unknown' };
        })
      };
    });
    return activity;
  }

  // Add status client
  addStatusClient(ws, subscribedChannel = null) {
    this.statusClients.add(ws);
    if (subscribedChannel !== null) {
      this.statusClientChannels.set(ws, subscribedChannel);
      console.log(`Status client subscribed to channel: ${subscribedChannel}`);
    } else {
      console.log('Status client subscribed to all channels');
    }
  }

  // Remove status client
  removeStatusClient(ws) {
    this.statusClients.delete(ws);
    this.statusClientChannels.delete(ws);
  }

  // Notify status clients (only those subscribed to the channel)
  notifyStatusClients(notification) {
    const message = JSON.stringify(notification);
    const notificationChannel = notification.channel;
    
    this.statusClients.forEach((client) => {
      if (client.readyState === WebSocket.OPEN) {
        const subscribedChannel = this.statusClientChannels.get(client);
        
        // Send notification if:
        // 1. Client has no channel filter (subscribed to all), OR
        // 2. Client is subscribed to this specific channel
        if (subscribedChannel === undefined || subscribedChannel === notificationChannel) {
          client.send(message);
        }
      } else {
        this.statusClients.delete(client);
        this.statusClientChannels.delete(client);
      }
    });
  }

  // Generate unused channel number
  generateChannelNumber(preferred = null, lastClientId = null) {
    // Check if lastClientId is still connected to the preferred channel
    if (preferred && lastClientId) {
      const channelClients = this.channels.get(preferred);
      if (channelClients) {
        // Check if any client in this channel has the matching clientId
        for (const client of channelClients) {
          if (client.clientId === lastClientId && client.readyState === 1) { // WebSocket.OPEN = 1
            console.log(`Client ${lastClientId} is still connected to channel ${preferred}, maintaining connection`);
            // Return existing token for reconnect scenario
            const existingToken = this.channelTokens.get(preferred);
            return { 
              channel: preferred, 
              token: existingToken,
              digits: preferred.toString().length, 
              preferred: true, 
              reconnect: true 
            };
          }
        }
      }
    }

    // Check preferred channel first (normal case)
    if (preferred && !this.channels.has(preferred)) {
      const token = this.generateChannelToken();
      this.storeChannelToken(preferred, token);
      return { 
        channel: preferred, 
        token,
        digits: preferred.toString().length, 
        preferred: true, 
        reconnect: false 
      };
    }

    // Generate new channel number
    for (let digits = 4; digits <= 8; digits++) {
      // Try random generation
      for (let attempt = 0; attempt < 100; attempt++) {
        const min = Math.pow(10, digits - 1);
        const max = Math.pow(10, digits) - 1;
        const channel = Math.floor(Math.random() * (max - min + 1)) + min;
        
        if (!this.channels.has(channel)) {
          const token = this.generateChannelToken();
          this.storeChannelToken(channel, token);
          return { 
            channel, 
            token,
            digits, 
            preferred: false, 
            reconnect: false 
          };
        }
      }

      // Try sequential search
      const min = Math.pow(10, digits - 1);
      const max = Math.pow(10, digits) - 1;
      for (let i = min; i <= max; i++) {
        if (!this.channels.has(i)) {
          const token = this.generateChannelToken();
          this.storeChannelToken(i, token);
          return { 
            channel: i, 
            token,
            digits, 
            preferred: false, 
            reconnect: false 
          };
        }
      }
    }

    return null; // All channels exhausted
  }

  // Get health statistics
  getHealthStats() {
    let totalClients = 0;
    const channelStats = {};
    
    this.channels.forEach((clients, channelId) => {
      const activeClients = Array.from(clients).filter(client => client.readyState === WebSocket.OPEN);
      channelStats[channelId] = activeClients.length;
      totalClients += activeClients.length;
    });
    
    return {
      totalConnectedClients: totalClients,
      channelCount: this.channels.size,
      channels: channelStats
    };
  }
}

// ===== WEBSOCKET HANDLERS =====
class WebSocketHandlers {
  constructor(channelManager) {
    this.channelManager = channelManager;
  }

  // Handle data WebSocket connections
  handleDataConnection(ws, req) {
    let channel = 0;
    let clientId = null;
    
    try {
      const url = new URL(req.url, `http://${req.headers.host}`);
      const channelParam = url.searchParams.get('channel');
      channel = channelParam && !isNaN(channelParam) && parseInt(channelParam, 10) >= 0 
        ? parseInt(channelParam, 10) 
        : 0;
    } catch (error) {
      console.error('Error parsing WebSocket URL:', error);
      // Use default channel 0 if URL parsing fails
      channel = 0;
    }

    try {
      const result = this.channelManager.addClient(ws, channel, req.headers['user-agent']);
      clientId = result.clientId;
      
      console.log(`New client connected to channel ${channel} (ID: ${clientId})`);

      // Send welcome message
      ws.send(JSON.stringify({
        message: `Connected to WebSocket server on channel ${channel}`,
        channel,
        clientId,
        timestamp: new Date()
      }));
    } catch (error) {
      console.error('Error adding client:', error);
      ws.close(1011, 'Server error during connection setup');
      return;
    }

    // Handle messages (placeholder for future features)
    ws.on('message', (message) => {
      try {
        const data = JSON.parse(message.toString());
        console.log(`Received message from client ${clientId}:`, data);
      } catch (error) {
        console.error('Error parsing client message:', error);
      }
    });

    // Handle disconnect
    ws.on('close', (code, reason) => {
      console.log(`Client ${clientId} disconnected from channel ${channel} (code: ${code}, reason: ${reason})`);
      this.channelManager.removeClient(ws, 'disconnected');
    });

    // Handle errors
    ws.on('error', (error) => {
      console.error(`WebSocket error for client ${clientId}:`, error);
      this.channelManager.removeClient(ws, 'error');
    });
  }

  // Handle status WebSocket connections
  handleStatusConnection(ws, req) {
    const url = new URL(req.url, `http://${req.headers.host}`);
    const channelParam = url.searchParams.get('channel');
    
    let subscribedChannel = null;
    if (channelParam) {
      const parsed = parseInt(channelParam.trim(), 10);
      if (!isNaN(parsed) && parsed >= 0) {
        subscribedChannel = parsed;
      }
    }
    
    console.log('New status client connected');
    this.channelManager.addStatusClient(ws, subscribedChannel);

    // Send initial status
    const initialMessage = {
      type: 'initial_status',
      message: subscribedChannel !== null 
        ? `Connected to status WebSocket server, subscribed to channel: ${subscribedChannel}`
        : 'Connected to status WebSocket server, subscribed to all channels',
      subscribedChannel,
      channelActivity: this.channelManager.getChannelActivity(),
      timestamp: new Date()
    };

    // Filter initial activity to subscribed channel if specified
    if (subscribedChannel !== null) {
      const filteredActivity = {};
      if (initialMessage.channelActivity[subscribedChannel]) {
        filteredActivity[subscribedChannel] = initialMessage.channelActivity[subscribedChannel];
      }
      initialMessage.channelActivity = filteredActivity;
    }

    ws.send(JSON.stringify(initialMessage));

    // Handle queries
    ws.on('message', (message) => {
      try {
        const data = JSON.parse(message.toString());
        this.handleStatusQuery(ws, data);
      } catch (error) {
        console.error('Error parsing status client message:', error);
        ws.send(JSON.stringify({
          type: 'error',
          message: 'Invalid message format',
          timestamp: new Date()
        }));
      }
    });

    // Handle disconnect
    ws.on('close', () => {
      console.log('Status client disconnected');
      this.channelManager.removeStatusClient(ws);
    });

    // Handle errors
    ws.on('error', (error) => {
      console.error('Status WebSocket error:', error);
      this.channelManager.removeStatusClient(ws);
    });
  }

  // Handle status queries
  handleStatusQuery(ws, data) {
    if (data.query === 'channel_activity') {
      ws.send(JSON.stringify({
        type: 'channel_activity_response',
        channelActivity: this.channelManager.getChannelActivity(),
        timestamp: new Date()
      }));
    } else if (data.query === 'channel_info' && data.channel !== undefined) {
      const channelId = parseInt(data.channel, 10);
      const activity = this.channelManager.getChannelActivity();
      const channelInfo = activity[channelId] || { clientCount: 0, clients: [] };
      
      ws.send(JSON.stringify({
        type: 'channel_info_response',
        channel: channelId,
        ...channelInfo,
        timestamp: new Date()
      }));
    }
  }
}

// ===== MAIN APPLICATION =====
const app = express();
const server = http.createServer(app);
const channelManager = new ChannelManager();
const wsHandlers = new WebSocketHandlers(channelManager);

// CORS middleware
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization, content-type');
  
  if (req.method === 'OPTIONS') {
    res.sendStatus(200);
  } else {
    next();
  }
});

app.use(express.json());

// WebSocket server - Single server with simple configuration for better compatibility
const wss = new WebSocket.Server({ server });

// Handle all WebSocket connections
wss.on('connection', (ws, req) => {
  const url = new URL(req.url, `http://${req.headers.host}`);
  const isStatusConnection = url.searchParams.get('status') === 'true';
  
  if (isStatusConnection) {
    wsHandlers.handleStatusConnection(ws, req);
  } else {
    wsHandlers.handleDataConnection(ws, req);
  }
});

// ===== API ENDPOINTS =====

// Broadcast endpoint
app.post('/broadcast', (req, res) => {
  try {
    const channel = req.query.channel !== undefined ? parseInt(req.query.channel, 10) : 0;
    const token = req.query.token;
    
    if (isNaN(channel) || channel < 0) {
      return res.status(400).json({ 
        success: false, 
        error: 'Invalid channel number. Channel must be a non-negative integer.' 
      });
    }

    // Validate token
    if (!token) {
      return res.status(401).json({
        success: false,
        error: 'Access token is required. Please provide a valid token for this channel.',
        channel
      });
    }

    if (!channelManager.validateChannelToken(channel, token)) {
      return res.status(403).json({
        success: false,
        error: 'Invalid or expired token for this channel. Please generate a new channel number.',
        channel
      });
    }

    const broadcastData = {
      ...req.body,
      timestamp: new Date(),
      source: 'http-endpoint'
    };

    const sentCount = channelManager.broadcastToChannel(channel, broadcastData);

    res.json({ 
      success: true, 
      message: `Data broadcasted successfully to channel ${channel}`,
      channel,
      clientCount: sentCount,
      data: broadcastData
    });

  } catch (error) {
    console.error('Error broadcasting data:', error);
    res.status(500).json({ 
      success: false, 
      error: 'Failed to broadcast data' 
    });
  }
});

// Health endpoint
app.get('/health', (req, res) => {
  res.json({ 
    status: 'healthy', 
    ...channelManager.getHealthStats(),
    timestamp: new Date()
  });
});

// Generate channel endpoint
app.get('/generate-channel', (req, res) => {
  try {
    const preferredParam = req.query.preferred;
    const lastClientIdParam = req.query.lastClientId;
    
    const preferred = preferredParam && !isNaN(preferredParam) && parseInt(preferredParam, 10) > 0 
      ? parseInt(preferredParam, 10) 
      : null;

    const lastClientId = lastClientIdParam && lastClientIdParam.trim().length > 0 
      ? lastClientIdParam.trim() 
      : null;

    if (preferredParam && !preferred) {
      return res.status(400).json({
        success: false,
        error: 'Invalid preferred channel number. Must be a positive integer.',
        timestamp: new Date()
      });
    }

    const result = channelManager.generateChannelNumber(preferred, lastClientId);
    
    if (!result) {
      return res.status(503).json({
        success: false,
        error: 'All channel numbers up to 8 digits are currently in use',
        timestamp: new Date()
      });
    }

    let message;
    if (result.reconnect) {
      message = `Client ${lastClientId} is still connected to preferred channel ${result.channel}, maintaining connection`;
    } else if (result.preferred) {
      message = `Preferred channel number ${result.channel} is available`;
    } else if (preferred) {
      message = `Preferred channel ${preferred} was taken, generated alternative ${result.digits}-digit channel number: ${result.channel}`;
    } else {
      message = `Generated unused ${result.digits}-digit channel number: ${result.channel}`;
    }

    res.json({
      success: true,
      channel: result.channel,
      token: result.token,
      digits: result.digits,
      preferred: result.preferred,
      reconnect: result.reconnect || false,
      requestedPreferred: preferred,
      lastClientId: lastClientId,
      message,
      timestamp: new Date()
    });

  } catch (error) {
    console.error('Error generating channel number:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to generate channel number',
      timestamp: new Date()
    });
  }
});

// ===== SERVER STARTUP =====
const PORT = process.env.PORT || 8080;
server.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
  console.log(`WebSocket server is running on ws://localhost:${PORT}`);
  console.log('Available endpoints:');
  console.log(`  POST http://localhost:${PORT}/broadcast?channel=<number>&token=<token> - Send JSON data with valid token`);
  console.log(`  GET  http://localhost:${PORT}/health - Check server status`);
  console.log(`  GET  http://localhost:${PORT}/generate-channel?preferred=<number>&lastClientId=<id> - Get channel number and token`);
  console.log('WebSocket usage:');
  console.log(`  Connect to data channel: ws://localhost:${PORT}?channel=<number>`);
  console.log(`  Connect to status updates (all channels): ws://localhost:${PORT}?status=true`);
  console.log(`  Connect to status updates (specific channel): ws://localhost:${PORT}?status=true&channel=<number>`);
  console.log('Security:');
  console.log('  - Channel tokens are required for broadcasting');
  console.log('  - Generate channel numbers to receive tokens');
  console.log('  - Tokens are automatically cleaned up when channels become empty');
});
import Foundation
import Starscream
import Pulsar

class WebSocketManager: ObservableObject, WebSocketDelegate {
  
  var socket: WebSocket?
  var socketPlayground: WebSocket?
  @Published var message: String = ""
  @Published var statusInfo: String = "You are not connected 😕"
  let pulsar: Pulsar = Pulsar()
  var composer: PatternComposerImpl? = nil
  var channel: String = ""
  var isConnected: Bool = false
  var playAnimation: Bool = false
  
  func connect() {
    // var request = URLRequest(url: URL(string: "wss://haptics-server.onrender.com?channel=" + channel)!)
    var request: URLRequest;
    var token = UserDefaults.standard.string(forKey: "token")
    if (channel != "") {
      token = nil;
    }
    if (token == nil) {
//      request = URLRequest(url: URL(string: "ws://192.168.92.124:8080?type=receiver&action=new_connection&code=" + channel)!)
      request = URLRequest(url: URL(string: "ws://localhost:8080?type=receiver&action=new_connection&code=" + channel)!)
      request.timeoutInterval = 5
    } else {
//      request = URLRequest(url: URL(string: "ws://192.168.92.124:8080?type=receiver&action=reuse_connection&token=" + token!)!)
      request = URLRequest(url: URL(string: "ws://localhost:8080?type=receiver&action=reuse_connection&token=" + token!)!)
      request.timeoutInterval = 5
    }
    
    
    UserDefaults.standard.set(channel, forKey: "channel")
    
    statusInfo = "Connecting to server..."
    
    socket = WebSocket(request: request)
    socket?.delegate = self
    socket?.connect()
    
    DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
      if (self.isConnected == true ) { return }
      self.statusInfo = "Unable to server connect.\nPlease try again later."
    }
    
//    connectToPlayground()
    composer = pulsar.PatternComposer()
  }
  
  func connectToPlayground() {
    var request = URLRequest(url: URL(string: "ws://192.168.92.124:8080")!)
    request.timeoutInterval = 5
    socketPlayground = WebSocket(request: request)
    socketPlayground?.delegate = self
    socketPlayground?.connect()
  }

  func didReceive(event: Starscream.WebSocketEvent, client: any Starscream.WebSocketClient) {
    switch event {
    case .connected(_):
        print("WebSocket connected")
    case .disconnected(let reason, let code):
        print("WebSocket disconnected: \(reason) with code: \(code)")
        self.isConnected = false
        self.statusInfo = "You are not connected 😕"
    case .text(let jsonData):
        DispatchQueue.main.async {
          self.isConnected = true
          self.message = jsonData
          print("Received text: \(jsonData)")
          
          // Parse JSON to check for event types
          if let data = jsonData.data(using: .utf8),
             let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
             let eventType = json["type"] as? String {
            
            switch eventType {
            case "connection_established":
              if let token = json["token"] as? String {
                UserDefaults.standard.set(token, forKey: "token")
                self.statusInfo = "Connection established! 🎉"
                print("Token saved: \(token)")
              }
              
            case "connection_restored":
              self.statusInfo = "Connection restored! 🎉"
              print("Connection restored")
              
            case "peer_disconnected":
              self.statusInfo = "Peer disconnected 😕"
              print("Peer disconnected")
              
            default:
              // Handle broadcast messages with patterns
              self.statusInfo = "You are connected! 🎉"
              let pattern = self.composer?.parseJSON(jsonData)
              if (pattern != nil) {
                self.composer?.playPattern(hapticsData: pattern!);
              }
            }
          } else {
            // Fallback for non-JSON or pattern data
            self.statusInfo = "You are connected! 🎉"
            let pattern = self.composer?.parseJSON(jsonData)
            if (pattern != nil) {
              self.composer?.playPattern(hapticsData: pattern!);
            }
          }
        }
        
    case .error(let error):
        print("WebSocket error: \(String(describing: error))")
        statusInfo = "Unable to server connect.\nPlease try again later."
    default:
        break
    }
  }

  deinit {
    socket?.disconnect()
    socket?.delegate = nil
    
    socketPlayground?.disconnect()
    socketPlayground?.delegate = nil
  }
}

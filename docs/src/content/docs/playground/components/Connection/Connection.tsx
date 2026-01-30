import { useEffect, useRef, useState } from "react";
import { API_SERVER_URL, SOCKET_SERVER_URL } from "../config";

import style from './Connection.module.scss';
import commonStyle from '../common.module.scss';
import refreshIcon from '../../../assets/new_assets/refresh.svg';
import { Accordion } from "../Accordion/Accordion";
import { Point } from "../Point/Point";

declare global {
  interface Window {
    connectionChannel: number;
  }
}

export default function Connection() {
  const [channel, setChannel] = useState<number | string>('Pulsar App already set up.');
  const [status, setStatus] = useState<boolean>(false);
  const ws = useRef<WebSocket | null>(null);

  function createChannel() {
    setChannel('Loading...');
    fetch(`${API_SERVER_URL}/create-channel`)
    .then(response => response.json())
    .then(data => {
      if (!data.success) {
        console.error('Failed to get channel from server');
        return;
      }
      const channelNumber = data.code;
      setChannel(channelNumber);
  
      webSocketConnection(channelNumber)
    });
  }

  function webSocketConnection(channelNumber?: string) {
    let token = null;
    if (localStorage.getItem('hapticsToken')) {
      token = localStorage.getItem('hapticsToken');
    }
    let params = '';
    if (token) {
      params = `&action=reuse_connection&token=${token}`;
    } else {
      params = `&action=new_connection&code=${channelNumber}`;
    }
    
    if (ws.current !== null) {
      ws.current.close();
    }
    ws.current = new WebSocket(`${SOCKET_SERVER_URL}?type=sender${params}`);
    ws.current.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log(data)
      switch(data.type) {
        case 'connection_established': {
          localStorage.setItem('hapticsToken', data.token);
          setStatus(true);
        } break;
        case 'connection_restored': {
          setStatus(true);
        } break;
        case 'peer_disconnected': {
          setStatus(false);
        } break;
      };
    }
  }
  
  useEffect(() => {
    if (localStorage.getItem('hapticsToken')) {
      webSocketConnection();
    } else {
      createChannel();
    }
    return () => {
      if (ws.current) {
        ws.current.close();
      }
    };
  }, []);

  function handleReset() {
    localStorage.removeItem('hapticsToken');
    setStatus(false);
    createChannel();
  }

  return <div className={['not-content', style.background].join(' ')}>
    <div className={style.content}>

      <div className={style.title}>Connect device</div>
      <div className={style.subtitle}>Connect your haptic device first. Pair it with the app now so you can test the presets.</div>

      <div className={style.codebox}>
        <div className={style.prompt}>Your pairing code:</div>
        <div className={style.code}>9210</div>
        <div className={style.status}>
          <div className={style.desc}>Device not connected</div>
          <div className={style.indicator}></div>
        </div>
      </div>

      <Accordion title="How to connect a device? 🤔">
        <Point index={1}>
          <div>
            Download the PulsarApp for App Store or Play Store.
          </div>
        </Point>
        <Point index={2}>
          <div>
            Open Playground and find Device Connection section.
          </div>
        </Point>
        <Point index={3}>
          <div>
            Type Paring code into PulsarApp and click Connect button.
          </div>
        </Point>
      </Accordion>

    </div>
  </div>;
}
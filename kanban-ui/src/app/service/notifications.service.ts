import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private client: Client;

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 5000,
    });

    this.client.onConnect = () => {
      this.client.subscribe('/topic/messages', (message) => {
        console.log('Received: ' + message.body);
      });
    };

    this.client.activate();
  }
}

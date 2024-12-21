import { Injectable } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';


@Injectable({
  providedIn: 'root'
})
export class RabbitMQService {
  private client: Client;

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://' + window.location.hostname + ':15674/ws',
      connectHeaders: {
        login: 'myuser',
        passcode: 'secret'
      },
      debug: (str) => {
        console.log(str);
      },
      onConnect: (frame) => {
        console.log('Connected to RabbitMQ');
        this.subscribeToQueue();
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      }
    });
  }

  connect() {
    this.client.activate();
  }

  private subscribeToQueue(): void {
    const subscription: StompSubscription = this.client.subscribe('/queue/notifications', (message) => {
      console.log('Received message:', message.body);
      this.showNotification(message.body)
    });
  }

  private showNotification(text: string) {
    // Automatically remove the notification after 3 seconds
      console.log("creating notification")
      const notification = document.createElement('div');
      notification.style.position = 'fixed';
      notification.style.top = '20px';
      notification.style.right = '20px';
      notification.style.backgroundColor = '#4caf50';
      notification.style.color = 'white';
      notification.style.padding = '15px';
      notification.style.borderRadius = '5px';
      notification.style.boxShadow = '0px 4px 8px rgba(0,0,0,0.2)';
      notification.style.fontSize = '16px';
      notification.style.zIndex = '9999';
      notification.innerText = text;
      document.body.appendChild(notification);
      console.log("appending")

      setTimeout(() => {
        notification.remove();
        window.location.reload()
      }, 3000);
  }

}

import { Injectable } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
// Import service from the library
import { ToastEvokeService } from '@costlydeveloper/ngx-awesome-popup';


@Injectable({
  providedIn: 'root'
})
export class RabbitMQService {
  private client: Client;

  constructor(private toast: NgToastService) {
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
      this.toast.success({detail:'Success',summary:message.body, sticky:true,position:'tr'})
    });
  }

}

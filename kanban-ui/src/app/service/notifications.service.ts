import { Injectable } from '@angular/core';
import { AMQPWebSocketClient } from './js/amqp-websocket-client.mjs'

@Injectable({
  providedIn: 'root'
})
export class RabbitMQService {

  private amqp: AMQPWebSocketClient = new AMQPWebSocketClient("ws://rabbitmq:15674/ws", "/", "myuser", "secret")
  constructor() {}

  async connect(): Promise<void> {
    console.log("connection")

    const conn = await this.amqp.connect()
    const ch = await conn.channel()
    console.log("channel"+ch)

    const q = await ch.queue("notifications")
    console.log("queie"+q)
    const consumer = await q.subscribe({noAck: false}, (msg) => {
      console.log(msg)
      msg.ack()
    })
  }
}

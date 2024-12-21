import { Component, OnInit, OnDestroy } from '@angular/core';
import { RabbitMQService } from './service/notifications.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'kanban-ui';

  constructor(private rabbitMQService: RabbitMQService) {}

  ngOnInit(): void {
    // Connect to RabbitMQ when the component is initialized
    this.rabbitMQService.connect();
  }
}

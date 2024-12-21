package io.github.aljolen.kanban.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration{
    @Bean
    public Queue notificationsQueue() {
        return new Queue("notifications", true);
    }

    @Bean
    public Object stuff(MessageSender sender){
        sender.send();
        return new Object();
    }
}

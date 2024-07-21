package org.lio.userservicerr.infrastructure.services.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public Queue userQueue() {
        return new Queue("users-queue", false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("saga_users_exchange", false, false);
    }

    @Bean
    public Binding binding(Queue userQueue, DirectExchange exchange) {
        return BindingBuilder.bind(userQueue).to(exchange).with("getUserFromCar");
    }
}

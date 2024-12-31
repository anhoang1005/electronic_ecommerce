package com.example.ecommerce.rabbit;

import com.example.ecommerce.rabbit.elastic.OrderItemDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderItemProducer {
    private final AmqpTemplate amqpTemplate;
    private final String itemExchange;
    private final String itemQueue;
    private final String itemRoutingKey;

    public OrderItemProducer(
            AmqpTemplate amqpTemplate,
            @Value("${rabbitmq.exchange.item.name}") String itemExchange,
            @Value("${rabbitmq.queue.item}") String itemQueue,
            @Value("${rabbitmq.routing.item.key}") String itemRoutingKey
    ) {
        this.amqpTemplate = amqpTemplate;
        this.itemExchange = itemExchange;
        this.itemQueue = itemQueue;
        this.itemRoutingKey = itemRoutingKey;
    }

    public void sendItemToQueue(OrderItemDocument orderItemDocument) {
        amqpTemplate.convertAndSend(itemExchange, itemRoutingKey, orderItemDocument);
        log.info("Sent item to queue : " + orderItemDocument.getId());
    }
}

package com.example.ecommerce.rabbit;

import com.example.ecommerce.entities.OrdersShop;
import com.example.ecommerce.rabbit.elastic.OrderShopDocument;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProducer {
    private final AmqpTemplate amqpTemplate;
    private final String orderExchange;
    private final String orderQueue;
    private final String orderRoutingkey;

    public OrderProducer(AmqpTemplate amqpTemplate,
                         @Value("${rabbitmq.exchange.order.name}") String orderExchange,
                         @Value("${rabbitmq.queue.order}") String orderQueue,
                         @Value("${rabbitmq.routing.order.key}") String orderRoutingkey

    ) {
        this.amqpTemplate = amqpTemplate;
        this.orderExchange = orderExchange;
        this.orderQueue = orderQueue;
        this.orderRoutingkey = orderRoutingkey;
    }

    public void sendOrderToQueue(OrderShopDocument ordersShop) {
        amqpTemplate.convertAndSend(orderExchange, orderRoutingkey, ordersShop);
        log.info("Sent order to queue : " +  ordersShop.getId());
    }
}

package com.example.ecommerce.rabbit;

import com.example.ecommerce.entities.elastic.ProductDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducer {
    private final AmqpTemplate amqpTemplate;
    private final String exchange;
    private final String routingKey;

    public ProductProducer (AmqpTemplate amqpTemplate,
                            @Value("${rabbitmq.exchange.name}") String exchange,
                            @Value("${rabbitmq.routing.product.key}") String routingKey){
        this.amqpTemplate = amqpTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }
    public void sendProductToQueue(ProductDocument product){
        //log.info(product.toString());
        amqpTemplate.convertAndSend(exchange, routingKey ,product);
        log.info("Product sent to rabbitmq : " + product.getId());
    }

    public void sendProductToRecommendQueue(ProductDocument product){
        amqpTemplate.convertAndSend(exchange, routingKey ,product);
    }
}


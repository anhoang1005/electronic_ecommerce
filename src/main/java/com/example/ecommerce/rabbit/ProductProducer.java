package com.example.ecommerce.rabbit;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.elastic.ProductDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class ProductProducer {

    private final AmqpTemplate amqpTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.product.key}")
    private String routingKey;

    public ProductProducer (AmqpTemplate amqpTemplate){
        this.amqpTemplate = amqpTemplate;
    }

    public void sendProductToQueue(ProductDocument product){
        amqpTemplate.convertAndSend(exchange,routingKey,product);
        log.info("Product sent to rabbitmq :" + product.getTitle());
    }
}

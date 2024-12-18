package com.example.ecommerce.rabbit;

import com.example.ecommerce.rabbit.elastic.ProductDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducer {
    private final AmqpTemplate amqpTemplate;
    private final String productExchange;
    private final String productRoutingKey;

    private final String recommendExchange;
    private final String recommendRoutingKey;
    private final String productRecommendRoutingKey;

    public ProductProducer(AmqpTemplate amqpTemplate,
                           @Value("${rabbitmq.exchange.product.name}") String productExchange,
                           @Value("${rabbitmq.routing.product.key}") String productRoutingKey,
                           @Value("${rabbitmq.exchange.recommend.name}") String recommendExchange,
                           @Value("${rabbitmq.routing.recommend.key}") String recommendRoutingKey,
                           @Value("${rabbitmq.routing.product.recommend.key}") String productRecommendRoutingKey){
        this.amqpTemplate = amqpTemplate;
        this.productExchange = productExchange;
        this.productRoutingKey = productRoutingKey;
        this.recommendExchange = recommendExchange;
        this.recommendRoutingKey = recommendRoutingKey;
        this.productRecommendRoutingKey = productRecommendRoutingKey;
    }

    public void sendProductToQueue(ProductDocument product){
        amqpTemplate.convertAndSend(productExchange, productRoutingKey, product);
        amqpTemplate.convertAndSend(recommendExchange, productRecommendRoutingKey, product);
        log.info("Product sent to rabbitmq : " + product.getId());
    }
}


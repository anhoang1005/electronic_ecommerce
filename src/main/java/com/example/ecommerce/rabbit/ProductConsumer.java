package com.example.ecommerce.rabbit;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class ProductConsumer {
//    @RabbitListener(queues = "${rabbitmq.queue.product}")
//    public void consumeProductFromQueue(String message) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            ProductDocument product = objectMapper.readValue(message, ProductDocument.class);
//            log.info("Product consumed and saved: " + product.getTitle());
//        } catch (Exception e) {
//            log.error("Failed to consume product from queue: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//}

package com.example.ecommerce.rabbit;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.repository.elastic.ProductDocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Slf4j
public class ProductConsumer {
    private final ProductDocumentRepository productDocumentRepository;

    @Autowired
    public ProductConsumer(ProductDocumentRepository productDocumentRepository){
        this.productDocumentRepository = productDocumentRepository;

    }
    @RabbitListener(queues = "${rabbitmq.queue.product}")
    public void consumeProductFromQueue(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDocument product = objectMapper.readValue(message, ProductDocument.class);
            productDocumentRepository.save(product);
            log.info("Product consumed and saved: " + product.getTitle());
        } catch (Exception e) {
            log.error("Failed to consume product from queue: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

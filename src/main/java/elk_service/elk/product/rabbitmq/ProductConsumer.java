package elk_service.elk.product.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import elk_service.elk.product.entites.ProductDocument;
import elk_service.elk.product.rabbitmq.messageTemplate.ProductTemplate;
import elk_service.elk.product.rabbitmq.state.ProductElkState;
import elk_service.elk.product.repository.ProductDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            ProductDocument productDocument = objectMapper.readValue(message, ProductDocument.class);

            productDocumentRepository.save(productDocument);
            log.info("Product consumed and saved: " + productDocument.getTitle());
            log.info("data received to rabbit :{}",productDocument.getShopCategoryIds());
        } catch (Exception e) {
            log.error("Failed to consume product from queue: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

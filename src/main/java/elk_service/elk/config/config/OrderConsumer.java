package elk_service.elk.config.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import elk_service.elk.order.entites.OrderShopDocument;
import elk_service.elk.order.repository.OrderDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {
    private final OrderDocumentRepository orderDocumentRepository;

    @Autowired
    public OrderConsumer(OrderDocumentRepository orderDocumentRepository) {
        this.orderDocumentRepository = orderDocumentRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.order}")
    public void consumeOrderFromQueue(String message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            OrderShopDocument orderShopDocument = objectMapper.readValue(message, OrderShopDocument.class);

            orderDocumentRepository.save(orderShopDocument);
            log.info("Product consumed and saved: " + orderShopDocument.getId());
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}

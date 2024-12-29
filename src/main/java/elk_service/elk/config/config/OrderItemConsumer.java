package elk_service.elk.config.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import elk_service.elk.order.entites.OrderItemDocument;
import elk_service.elk.order.repository.OrderItemDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderItemConsumer {
    private final OrderItemDocumentRepository orderItemDocumentRepository;
    @Autowired
    public OrderItemConsumer(OrderItemDocumentRepository orderItemDocumentRepository) {
        this.orderItemDocumentRepository = orderItemDocumentRepository;
    }
    @RabbitListener(queues = "${rabbitmq.queue.item}")
    public void consumerOrderItem(String message) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            OrderItemDocument orderItemDocument = mapper.readValue(message, OrderItemDocument.class);

            orderItemDocumentRepository.save(orderItemDocument);
            log.info("Order item consume and saved: " + orderItemDocument.getId());
        }catch (Exception e){
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}

package elk_service.elk.order.service;

import elk_service.elk.order.repository.OrderDocumentRepository;
import elk_service.elk.order.repository.OrderItemDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDocumentService {
    @Autowired
    private OrderDocumentRepository orderDocumentRepository;
    @Autowired
    private OrderItemDocumentRepository orderItemDocumentRepository;


}

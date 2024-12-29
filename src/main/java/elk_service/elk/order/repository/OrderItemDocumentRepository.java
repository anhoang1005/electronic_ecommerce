package elk_service.elk.order.repository;

import elk_service.elk.order.entites.OrderItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemDocumentRepository extends ElasticsearchRepository<OrderItemDocument, Long> {
}

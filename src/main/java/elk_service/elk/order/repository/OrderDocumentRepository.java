package elk_service.elk.order.repository;

import elk_service.elk.order.entites.OrderShopDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDocumentRepository extends ElasticsearchRepository<OrderShopDocument, String> {
}

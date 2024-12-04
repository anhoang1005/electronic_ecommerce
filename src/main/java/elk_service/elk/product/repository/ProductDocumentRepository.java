package elk_service.elk.product.repository;

import elk_service.elk.product.entites.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument,Long> {
    ProductDocument findProductDocumentByProductCode(String productCode);
}

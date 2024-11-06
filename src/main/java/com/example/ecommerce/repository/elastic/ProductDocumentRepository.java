package com.example.ecommerce.repository.elastic;

import com.example.ecommerce.entities.elastic.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument,Long> {
}

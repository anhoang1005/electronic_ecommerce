package com.example.ecommerce.service.elastic;

import com.example.ecommerce.entities.Product;

public interface ProductDocumentService {
    void saveProductToElasticSearch(Product product);
}

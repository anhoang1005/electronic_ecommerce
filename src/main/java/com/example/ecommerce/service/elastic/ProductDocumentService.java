package com.example.ecommerce.service.elastic;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.elastic.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;

public interface ProductDocumentService {
    void saveProductToElasticSearch(Product product);
    Page<ProductDocument> filterProduct(
            Long shopCategoryId,
            String updateAt,
            Double rating,
            Long sold,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
    Page<ProductDocument> filterAndSortProduct(
           Long shopId,
           Long shopCategoryId,
           String sortField,
           Pageable pageable,
           BigDecimal minPrice, BigDecimal maxPrice
    );
    void fullAsync();
    Page<ProductDocument> searchProductDocument(String searchText, Pageable pageable);
}

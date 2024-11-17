package com.example.ecommerce.service.elastic.impl;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.repository.elastic.ProductDocumentRepository;
import com.example.ecommerce.service.elastic.ProductDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDocumentServiceImpl implements ProductDocumentService {
    private final ProductDocumentRepository productDocumentRepository;
    @Override
    public void saveProductToElasticSearch(Product product) {
        productDocumentRepository.save(ProductMapper.mapperProductToDocument(product));
    }
}

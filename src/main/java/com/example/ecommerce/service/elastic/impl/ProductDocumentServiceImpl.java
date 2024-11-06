package com.example.ecommerce.service.elastic.impl;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ShopCategoryProduct;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.elastic.ProductDocumentRepository;
import com.example.ecommerce.service.elastic.ProductDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDocumentServiceImpl implements ProductDocumentService {
    private final ProductDocumentRepository productDocumentRepository;
    private final ProductRepository productRepository;
    @Override
    public void saveProductToElasticSearch(Product product) {
        ProductDocument productDocument = ProductDocument
                .builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .brandId(product.getBrand().getId())
                .title(product.getTitle())
                .slug(product.getProductSlug())
                .rating(product.getRating())
                .maxPrice(product.getMaxInitialPrice())
                .minPrice(product.getMinInitialPrice())
                .attributeValues(product.getProductAttributeValues())
                .productState(product.getProductState())
                .variants(product.getVariants())
                .shopCategoryIds(product.getShopCategoryProducts()
                        .stream().map(ShopCategoryProduct::getId).collect(Collectors.toList()))
                .categoryIds(product.getCategory().getId())
                .productUrl(product.getListImageOfProduct()
                        .stream().filter(image -> image.getIsMainImage())
                        .map(image -> image.getImageUrl()).findFirst().orElse(null))
                .build();

        productDocumentRepository.save(productDocument);
    }
}

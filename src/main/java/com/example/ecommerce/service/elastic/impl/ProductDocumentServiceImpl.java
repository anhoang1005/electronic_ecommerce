package com.example.ecommerce.service.elastic.impl;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.entities.enums.ErrorMessage;
import com.example.ecommerce.exceptions.BaseException;
import com.example.ecommerce.mapper.product.ProductMapper;
import com.example.ecommerce.repository.elastic.ProductDocumentRepository;
import com.example.ecommerce.service.elastic.ProductDocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.search.sort.SortOrder;
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDocumentServiceImpl implements ProductDocumentService {
    private final ProductDocumentRepository productDocumentRepository;
    private final RestHighLevelClient client;
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    @Override
    public void saveProductToElasticSearch(Product product) {
        productDocumentRepository.save(ProductMapper.mapperProductToDocument(product));
    }

    @Override
    public Page<ProductDocument> filterProduct(Long shopCategoryId, String updateAt, Double rating, Long sold, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(shopCategoryId != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("shopCategoryIds",shopCategoryId));
        }


        if(updateAt != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
            Date formated;
            try {
                formated = dateFormat.parse(updateAt);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: " + DATE_PATTERN);
            }
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("updatedAt").lte(formated));
        }

        if(rating != null){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("rating").gte(rating));
        }

        if(minPrice != null && maxPrice != null){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("minPrice").gte(minPrice.doubleValue()));
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("maxPrice").lte(maxPrice.doubleValue()));
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(pageable.getPageNumber() * pageable.getPageSize());
        searchSourceBuilder.size(pageable.getPageSize() > 0 ? pageable.getPageSize() : 20);

        SearchRequest searchRequest = new SearchRequest("product-document");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;
        try{
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            List<ProductDocument> results = Arrays.stream(searchResponse.getHits().getHits())
                    .map(hit -> {
                        String sourceAsString = hit.getSourceAsString();

                        try {
                            return new ObjectMapper().readValue(sourceAsString,ProductDocument.class);
                        }catch (JsonProcessingException e){
                            throw new RuntimeException("error paring search hit", e);
                        }
                    })
                    .collect(Collectors.toList());

            long totalHits = searchResponse.getHits().getTotalHits().value;

            Page<ProductDocument> page = new PageImpl<>(results,pageable,totalHits);
            return page;
        }catch (IOException e){
            throw new BaseException(ErrorMessage.ELASTICSEARCH_ERROR.name());
        }
    }

    @Override
    public Page<ProductDocument> filterAndSortProduct(Long shopId, Long shopCategoryId, String sortField, Pageable pageable, BigDecimal minPrice, BigDecimal maxPrice) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("shopId",shopId))
                .filter(QueryBuilders.termQuery("shopCategoryIds",shopCategoryId))
                .filter(QueryBuilders.termQuery("isDeleted",false));


        SortOrder sortOrder = SortOrder.ASC;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder);

        switch (sortField){
            case "updatedAt":
                searchSourceBuilder.sort("updatedAt",sortOrder);
                break;
            case "rating":
                searchSourceBuilder.sort("rating",sortOrder);
                break;
            case "price":
                if (minPrice != null && maxPrice != null) {
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("minPrice")
                            .gte(minPrice)
                            .lte(maxPrice));

                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("maxPrice").lte(maxPrice));
                }
                break;

        }
        searchSourceBuilder.from(pageable.getPageNumber() * pageable.getPageSize());
        searchSourceBuilder.size(pageable.getPageSize() > 0 ? pageable.getPageSize() : 10);

        SearchRequest searchRequest = new SearchRequest("product-document");
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try{
            searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);

            List<ProductDocument> productDocuments = Arrays.stream(searchResponse.getHits().getHits())
                    .map(hit -> {
                        String sourceAsString = hit.getSourceAsString();
                        try{
                            return new ObjectMapper().readValue(sourceAsString,ProductDocument.class);
                        }catch (JsonProcessingException e){
                            throw new RuntimeException("error paring search hit", e);
                        }

                    }).collect(Collectors.toUnmodifiableList());
            long totalHits = searchResponse.getHits().getTotalHits().value;
            Page<ProductDocument> page = new PageImpl<>(productDocuments,pageable,totalHits);
            return page;
        }catch (IOException e){
            throw new BaseException(ErrorMessage.ELASTICSEARCH_ERROR.name());
        }
    }
}

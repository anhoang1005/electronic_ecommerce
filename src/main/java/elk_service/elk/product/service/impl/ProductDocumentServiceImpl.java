package elk_service.elk.product.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elk_service.elk.product.entites.ProductDocument;
import elk_service.elk.product.repository.ProductDocumentRepository;
import elk_service.elk.product.service.ProductDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDocumentServiceImpl implements ProductDocumentService {
    private final ProductDocumentRepository productDocumentRepository;
    private final RestHighLevelClient client;

    private static final String DATE_PATTERN = "yyyy-MM-dd";


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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<ProductDocument> filterAndSortProduct(String shopCode, Long shopCategoryId, String sortField, Pageable pageable, BigDecimal minPrice, BigDecimal maxPrice) {
        BoolQueryBuilder boolQueryBuilder = buildDefaultFilters();
        boolQueryBuilder
                .filter(QueryBuilders.termQuery("shopCode",shopCode))
                .filter(QueryBuilders.termQuery("shopCategoryIds",shopCategoryId))
        ;



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

                    }).collect(Collectors.toList());
            long totalHits = searchResponse.getHits().getTotalHits().value;
            Page<ProductDocument> page = new PageImpl<>(productDocuments,pageable,totalHits);
            return page;
        }catch (IOException e){
//            throw new BaseException(ErrorMessage.ELASTICSEARCH_ERROR.name());
        }
        return null;
    }

    @Override
    public Page<ProductDocument> searchProductDocument(String searchText, Pageable pageable) {
                String[] keywords = searchText.toLowerCase().split("\\s+");

        BoolQueryBuilder mainQuery = buildDefaultFilters();

        // Exact match query
        BoolQueryBuilder exactMatchQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhraseQuery("title", searchText).boost(3.0f))
                .should(QueryBuilders.matchQuery("title", searchText).boost(1.5f))
                .should(QueryBuilders.matchQuery("categoryDocument.categoryName", searchText).boost(2.0f))
                .should(QueryBuilders.matchPhraseQuery("categoryDocument.categoryName",searchText).boost(1.0f))
                .should(QueryBuilders.matchQuery("brandDocument.brandName", searchText).boost(1.5f))
                .should(QueryBuilders.matchPhraseQuery("brandDocument.brandName",searchText).boost(0.75f))
                .should(QueryBuilders.nestedQuery(
                        "attributes",
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("attributes.attributeValue", searchText)),
                        ScoreMode.Max));

        // Partial match query
        BoolQueryBuilder partialMatchQuery = QueryBuilders.boolQuery();
        for (String keyword : keywords) {
            partialMatchQuery
                    .should(QueryBuilders.matchPhraseQuery(keyword,
                            "title"))
                    .should(QueryBuilders.matchPhraseQuery(keyword,
                            "categoryDocument.categoryName"))
                    .should(QueryBuilders.matchPhraseQuery(keyword,
                            "brandDocument.brandName"))
                    .should(QueryBuilders.nestedQuery(
                            "attributes",
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.matchQuery("attributes.attributeValue", keyword)),
                            ScoreMode.Max));
        }

        mainQuery.should(exactMatchQuery).boost(2.0f);
        mainQuery.should(partialMatchQuery).boost(1.0f);

        // Function score
        long now = Instant.now().toEpochMilli();
        FunctionScoreQueryBuilder scoreQuery = QueryBuilders.functionScoreQuery(mainQuery,
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder[] {
                                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                        ScoreFunctionBuilders.fieldValueFactorFunction("rating").factor(1.2f)),
                                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                        ScoreFunctionBuilders.exponentialDecayFunction("updatedAt", now, "7d", "0d", 0.5))
                        })
                .boostMode(CombineFunction.MULTIPLY);

        // Build search source
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(scoreQuery)
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize() > 0 ? pageable.getPageSize() : 10);

        // Elasticsearch search request
        SearchRequest searchRequest = new SearchRequest("product-document");
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // Map results
            List<ProductDocument> productDocuments = Arrays.stream(searchResponse.getHits().getHits())
                    .map(hit -> {
                        String sourceAsString = hit.getSourceAsString();
                        try {
                            ProductDocument productDocument = new ObjectMapper().readValue(sourceAsString, ProductDocument.class);
                            productDocument.setScore((double) hit.getScore()); // Gắn thêm score vào response
                            return productDocument;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error parsing search hit", e);
                        }
                    }).collect(Collectors.toList());

            long totalHits = searchResponse.getHits().getTotalHits().value;

            return new PageImpl<>(productDocuments, pageable, totalHits);
        } catch (IOException e) {
//            throw new BaseException(ErrorMessage.ELASTICSEARCH_ERROR.name());
            return null;
        }
    }
    //helper function
    public static BoolQueryBuilder buildDefaultFilters() {
        return QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery("isDeleted", true))
                .mustNot(QueryBuilders.termQuery("isHidden", true))
                .mustNot(QueryBuilders.termQuery("isAccepted", false))
                .mustNot(QueryBuilders.termQuery("isRejected", true));
    }

}
package elk_service.elk.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elk_service.elk.handle.ElasticsearchCustomException;
import elk_service.elk.order.dtos.params.OrderShopParams;
import elk_service.elk.order.dtos.response.OrderShopResponse;
import elk_service.elk.order.entites.OrderShopDocument;
import elk_service.elk.order.repository.OrderDocumentRepository;
import elk_service.elk.product.repository.ProductDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderShopOverviewService {
    @Autowired
    private OrderDocumentRepository orderDocumentRepository;
    @Autowired private ProductDocumentRepository productDocumentRepository;
    @Autowired private RestHighLevelClient restHighLevelClient;

    public Page<OrderShopDocument> filter(OrderShopParams params, Pageable pageable) {
        SearchRequest searchRequest = new SearchRequest("order-shop-document");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("shopId",params.getShopId()))
                .must(QueryBuilders.rangeQuery("updatedAt")
                        .gte(params.getStartDate())
                        .lte(params.getEndDate()));
        Map<String,String> paramsCheck = new HashMap<>();
        paramsCheck.put("status.keyword",params.getStatus());
        paramsCheck.put("deliveryMethod.keyword",params.getDeliveryMethod());

        paramsCheck.forEach((key,value) -> {
            if(value != null && !value.isEmpty()) boolQueryBuilder.must(QueryBuilders.matchQuery(key,value));
        });

        if (params.getOrderShopCode() != null && !params.getOrderShopCode().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("orderShopCode.keyword", "*" + params.getOrderShopCode() + "*"));
        }

        if (params.getOrderCode() != null && !params.getOrderCode().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("orderCode.keyword", "*" + params.getOrderCode() + "*"));
        }

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("updatedAt", SortOrder.DESC);
        searchSourceBuilder.from(pageable.getPageNumber());
        searchSourceBuilder.size(pageable.getPageSize());
        searchRequest.source(searchSourceBuilder);

        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<OrderShopDocument> orderShopDocuments = Arrays.stream(searchResponse.getHits().getHits())
                    .map(hit -> {
                        String sourceAsString = hit.getSourceAsString();
                        try{
                            return new ObjectMapper().readValue(sourceAsString,OrderShopDocument.class);
                        }catch (JsonProcessingException e){
                            throw new ElasticsearchCustomException(e.getMessage());
                        }
                    }).collect(Collectors.toList());
            long totalHits = searchResponse.getHits().getTotalHits().value;
            return new PageImpl<>(orderShopDocuments,pageable,totalHits);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }
}

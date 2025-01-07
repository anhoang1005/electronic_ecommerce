package elk_service.elk.order.service;

import elk_service.elk.handle.ElasticsearchCustomException;
import elk_service.elk.order.dtos.response.*;
import elk_service.elk.order.entites.enums.OrderStatus;
import elk_service.elk.order.entites.enums.ProductStatus;
import elk_service.elk.order.repository.OrderDocumentRepository;
import elk_service.elk.product.repository.ProductDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.aggregations.pipeline.BucketScriptPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.ParsedSimpleValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;



@Service
@Slf4j
public class OverviewService {
    @Autowired private OrderDocumentRepository orderDocumentRepository;
    @Autowired private ProductDocumentRepository productDocumentRepository;
    @Autowired private RestHighLevelClient restHighLevelClient;

    public ProductAndOrderOverview getProductAndProductOverview(Long shopId){
        return ProductAndOrderOverview
                .builder()
                .orderCancelled(countOrderByStatus(OrderStatus.DA_HUY.toString(),shopId))
                .orderCompleted(countOrderByStatus(OrderStatus.GH_THANH_CONG.name(),shopId))
                .orderPending(countOrderByStatus(OrderStatus.CHO_XAC_NHAN.name(),shopId))
                .orderProcessing(countOrderByStatus(OrderStatus.DANG_GIAO_HANG.name(),shopId))
                .orderDelivery(countOrderByStatus(OrderStatus.DANG_GIAO_HANG.name(),shopId))
                .lockedProduct(countProductByStatus(ProductStatus.LOCKED.name(),shopId))
                .soldOutProduct(countProductByStatus(ProductStatus.LOW_STOCK.name(),shopId))
                .build();
    }

    public MonthlyReportOrderOverview getMonthlyReportOrderOverview(Long shopId) {
        String indexName = "order-shop-document";
        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate now = LocalDate.now();

        String startDate = startOfYear.toString();
        String endDate = now.toString();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .must(QueryBuilders.termQuery("shopId", shopId))
                .must(QueryBuilders.rangeQuery("updatedAt").gte(startDate).lte(endDate));

        searchSourceBuilder.query(boolQueryBuilder);

        DateHistogramAggregationBuilder monthlyAgg = AggregationBuilders
                .dateHistogram("months")
                .field("updatedAt")
                .calendarInterval(DateHistogramInterval.MONTH)
                .format("yyyy-MM");

        TermsAggregationBuilder statusAgg = AggregationBuilders
                .terms("by_status")
                .field("status");

        monthlyAgg.subAggregation(statusAgg);
        searchSourceBuilder.aggregation(monthlyAgg);
        searchSourceBuilder.size(0);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            Histogram monthsAgg = searchResponse.getAggregations().get("months");
            List<String> months = new ArrayList<>();
            List<Long> completedOrders = new ArrayList<>();
            List<Long> canceledOrders = new ArrayList<>();

            for (Histogram.Bucket bucket : monthsAgg.getBuckets()) {
                months.add(bucket.getKeyAsString());

                Terms statusTerms = bucket.getAggregations().get("by_status");
                long completedCount = 0;
                long canceledCount = 0;

                for (Terms.Bucket statusBucket : statusTerms.getBuckets()) {
                    String status = statusBucket.getKeyAsString();
                    long docCount = statusBucket.getDocCount();

                    if (status.equals(OrderStatus.GH_THANH_CONG.name())) {
                        completedCount = docCount;
                    } else if (status.equals(OrderStatus.TRA_HANG.name())) {
                        canceledCount = docCount;
                    }
                }

                completedOrders.add(completedCount);
                canceledOrders.add(canceledCount);
            }

            return MonthlyReportOrderOverview
                    .builder()
                    .months(months)
                    .completedOrders(completedOrders)
                    .returnOrders(canceledOrders)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new ElasticsearchCustomException(e.getMessage());
        }
    }

    public MonthlyRevenueOverview getMonthlyRevenueOverview(Long shopId) {
        String indexName = "order-shop-document";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // Build query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("shopId", shopId))
                .must(QueryBuilders.termQuery("status", "GH_THANH_CONG"))
                .must(QueryBuilders.rangeQuery("updatedAt")
                        .gte("now/M")
                        .lte("now"));

        searchSourceBuilder.query(boolQueryBuilder);

        // Build aggregations
        DateHistogramAggregationBuilder monthlyAgg = AggregationBuilders
                .dateHistogram("monthly_stats")
                .field("updatedAt")
                .calendarInterval(DateHistogramInterval.MONTH)
                .format("yyyy-MM");

        // Add sub-aggregations for financial calculations
        monthlyAgg.subAggregation(AggregationBuilders.sum("total_amount").field("totalAmount"));
        monthlyAgg.subAggregation(AggregationBuilders.sum("total_discount").field("shopDiscount"));
        monthlyAgg.subAggregation(AggregationBuilders.sum("total_service_fee").field("serviceFee"));


        searchSourceBuilder.aggregation(monthlyAgg);
        searchSourceBuilder.size(0);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Histogram monthlyStats = searchResponse.getAggregations().get("monthly_stats");

            List<? extends Histogram.Bucket> buckets = monthlyStats.getBuckets();
            if (buckets.size() < 2) {
                return createEmptyResponse();
            }


            Histogram.Bucket currentMonth = buckets.get(buckets.size() - 1);
            Histogram.Bucket previousMonth = buckets.get(buckets.size() - 2);

            // Get current month values
            double totalAmount = ((ParsedSum) currentMonth.getAggregations().get("total_amount")).getValue();
            double totalDiscount = ((ParsedSum) previousMonth.getAggregations().get("total_discount")).getValue();
            double totalServiceFee = ((ParsedSum) currentMonth.getAggregations().get("total_service_fee")).getValue();

            double currentCost = totalServiceFee;
            double currentRevenue = totalAmount - totalDiscount;
            double currentProfit = currentRevenue - currentCost;

            if (buckets.size() < 2) {
                // Trả về dữ liệu tháng hiện tại, không có phần trăm thay đổi
                return MonthlyRevenueOverview.builder()
                        .totalRevenue(BigDecimal.valueOf(currentRevenue))
                        .totalCost(BigDecimal.valueOf(currentCost))
                        .totalProfit(BigDecimal.valueOf(currentProfit))
                        .revenuePercentageChange("N/A")
                        .costPercentageChange("N/A")
                        .profitPercentageChange("N/A")
                        .build();
            }

            // Get previous month values
            double previousAmount = ((ParsedSum) previousMonth.getAggregations().get("total_amount")).getValue();
            double previousDiscount = ((ParsedSum) previousMonth.getAggregations().get("total_discount")).getValue();
            double previousServiceFee = ((ParsedSum) currentMonth.getAggregations().get("total_service_fee")).getValue();

            double previousRevenue =  previousAmount - previousDiscount;
            double previousCost = previousServiceFee;
            double previousProfit = previousRevenue - previousCost;

            // Calculate percentage changes
            String revenueChange = calculatePercentageChange(previousRevenue, currentRevenue);
            String costChange = calculatePercentageChange(previousCost, currentCost);
            String profitChange = calculatePercentageChange(previousProfit, currentProfit);

            return MonthlyRevenueOverview.builder()
                    .totalRevenue(BigDecimal.valueOf(currentRevenue))
                    .totalCost(BigDecimal.valueOf(currentCost))
                    .totalProfit(BigDecimal.valueOf(currentProfit))
                    .revenuePercentageChange(revenueChange)
                    .costPercentageChange(costChange)
                    .profitPercentageChange(profitChange)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new ElasticsearchCustomException(e.getMessage());
        }
    }

    public List<MonthlyProductOverview> getMonthlyProductOverview(Long shopId) {
        // Tạo SearchRequest cho chỉ mục "order-item-document"
        SearchRequest searchRequest = new SearchRequest("order-item-document");

        // Xây dựng SearchSourceBuilder với size 0 vì chúng ta chỉ quan tâm đến aggregations
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        // Xây dựng truy vấn bool để lọc theo shopId
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("shopId", shopId));

        searchSourceBuilder.query(boolQuery);

        // Xây dựng aggregation "top_products" để nhóm theo productId và lấy 10 sản phẩm hàng đầu
        TermsAggregationBuilder topProductsAgg = AggregationBuilders.terms("top_products")
                .field("productId")
                .size(10)
                .order(BucketOrder.aggregation("current_month>current_month_quantity", false));

        // Aggregation "current_month" để lọc dữ liệu trong tháng hiện tại
        FilterAggregationBuilder currentMonthAgg = AggregationBuilders.filter("current_month",
                QueryBuilders.rangeQuery("orderDate")
                        .gte("now/M")
                        .lte("now"));

        // Tính tổng số lượng trong tháng hiện tại
        SumAggregationBuilder currentMonthQuantityAgg = AggregationBuilders.sum("current_month_quantity")
                .field("quantity");

        // Lấy thông tin chi tiết của sản phẩm
        TopHitsAggregationBuilder productDetailsAgg = AggregationBuilders.topHits("product_details")
                .size(1)
                .fetchSource(new String[] { "productId", "title", "price", "url" }, null);

        // Thêm sub-aggregations vào "current_month"
        currentMonthAgg.subAggregation(currentMonthQuantityAgg);
        currentMonthAgg.subAggregation(productDetailsAgg);

        // Aggregation "last_month_data" để lọc dữ liệu trong tháng trước
        FilterAggregationBuilder lastMonthDataAgg = AggregationBuilders.filter("last_month_data",
                QueryBuilders.rangeQuery("orderDate")
                        .gte("now-1M/M")
                        .lt("now/M"));

        // Tính tổng số lượng trong tháng trước
        SumAggregationBuilder lastMonthQuantityAgg = AggregationBuilders.sum("last_month_quantity")
                .field("quantity");

        // Thêm sub-aggregations vào "last_month_data"
        lastMonthDataAgg.subAggregation(lastMonthQuantityAgg);

        // Thêm các sub-aggregations vào "top_products"
        topProductsAgg.subAggregation(currentMonthAgg);
        topProductsAgg.subAggregation(lastMonthDataAgg);

        // Thêm aggregation "top_products" vào SearchSourceBuilder
        searchSourceBuilder.aggregation(topProductsAgg);

        // Gán SearchSourceBuilder vào SearchRequest
        searchRequest.source(searchSourceBuilder);

        try {
            // Thực hiện tìm kiếm
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // Khởi tạo danh sách kết quả
            List<MonthlyProductOverview> overviewList = new ArrayList<>();

            // Lấy aggregation "top_products"
            Terms topProducts = searchResponse.getAggregations().get("top_products");
            for (Terms.Bucket bucket : topProducts.getBuckets()) {
                // Lấy aggregation "current_month"
                Filter currentMonth = bucket.getAggregations().get("current_month");
                Sum currentMonthQuantity = currentMonth.getAggregations().get("current_month_quantity");
                double currentQuantity = currentMonthQuantity.getValue();

                // Lấy aggregation "last_month_data"
                Filter lastMonth = bucket.getAggregations().get("last_month_data");
                Sum lastMonthQuantity = lastMonth.getAggregations().get("last_month_quantity");
                double lastQuantity = lastMonthQuantity.getValue();

                // Lấy thông tin chi tiết sản phẩm từ "product_details"
                TopHits topHits = currentMonth.getAggregations().get("product_details");
                SearchHit[] hits = topHits.getHits().getHits();
                if (hits.length > 0) {
                    Map<String, Object> sourceAsMap = hits[0].getSourceAsMap();

                    // Lấy các trường thông tin sản phẩm
                    Long productId = null;
                    Object productIdObj = sourceAsMap.get("productId");
                    if (productIdObj instanceof Number) {
                        productId = ((Number) productIdObj).longValue();
                    } else if (productIdObj != null) {
                        productId = Long.parseLong(productIdObj.toString());
                    }

                    String title = (String) sourceAsMap.get("title");
                    String url = (String) sourceAsMap.get("url");

                    BigDecimal price = BigDecimal.ZERO;
                    Object priceObj = sourceAsMap.get("price");
                    if (priceObj instanceof Number) {
                        price = new BigDecimal(((Number) priceObj).doubleValue());
                    } else if (priceObj != null) {
                        price = new BigDecimal(priceObj.toString());
                    }

                    // Tạo đối tượng MonthlyProductOverview
                    MonthlyProductOverview overview = new MonthlyProductOverview();
                    overview.setId(productId);
                    overview.setTitle(title);
                    overview.setUrl(url);
                    overview.setPrice(price);
                    overview.setQuantity((long) currentQuantity);

                    // Tính toán sự khác biệt phần trăm
                    Double percentage = null;
                    if (lastQuantity != 0) {
                        percentage = ((currentQuantity - lastQuantity) / lastQuantity) * 100;
                    } else {
                        percentage = currentQuantity > 0 ? 100.0 : 0.0;
                    }
                    overview.setPercentage(percentage);

                    // Thêm vào danh sách kết quả
                    overviewList.add(overview);
                }
            }

            return overviewList;

        } catch (IOException e) {
            // Xử lý ngoại lệ
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<PaymentMethodOverview> getPaymentMethodOverviews(Long shopId) {
        SearchRequest searchRequest = new SearchRequest("order-shop-document");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("shopId",shopId));

        searchSourceBuilder.query(boolQueryBuilder);

        TermsAggregationBuilder paymentMethodAgg = AggregationBuilders.terms("payment_method")
                .field("paymentMethod")
                .size(10)
                .order(BucketOrder.count(false));

        searchSourceBuilder.aggregation(paymentMethodAgg);

        searchRequest.source(searchSourceBuilder);

        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

            List<PaymentMethodOverview> overviewList = new ArrayList<>();

            Terms paymentMethods = searchResponse.getAggregations().get("payment_method");
            for(Terms.Bucket bucket : paymentMethods.getBuckets()) {
                PaymentMethodOverview overview = new PaymentMethodOverview();
                overview.setPaymentMethod(bucket.getKeyAsString());
                overview.setCount(bucket.getDocCount());
                overviewList.add(overview);
            }

            return overviewList;
        }catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    public List<MonthlyReportProductOverview> getMonthlyReportProductOverviews(Long shopId) {
        SearchRequest searchRequest = new SearchRequest("order-item-document");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("shopId", shopId))
                .filter(QueryBuilders.rangeQuery("orderDate")
                        .gte("now/M")
                        .lte("now"));

        searchSourceBuilder.query(boolQuery);

        TermsAggregationBuilder productsSoldAgg = AggregationBuilders.terms("products_sold")
                .field("productId")
                .size(100)
                .order(BucketOrder.count(false));

        productsSoldAgg.subAggregation(AggregationBuilders.sum("total_quantity").field("quantity"));

        productsSoldAgg.subAggregation(AggregationBuilders.topHits("product_details")
                .size(1)
                .fetchSource(new String[]{"title"}, null));

        searchSourceBuilder.aggregation(productsSoldAgg);

        searchSourceBuilder.aggregation(AggregationBuilders.sum("total_products_sold").field("quantity"));

        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            Sum totalProductsSoldAgg = searchResponse.getAggregations().get("total_products_sold");
            double totalProductsSold = 0.0;
            if (totalProductsSoldAgg != null) {
                totalProductsSold = totalProductsSoldAgg.getValue();
                log.info("Total Products Sold: {}", totalProductsSold);
            } else {
                log.warn("Aggregation 'total_products_sold' is missing.");
            }

            Terms productsSold = searchResponse.getAggregations().get("products_sold");
            if (productsSold == null) {
                log.warn("Aggregation 'products_sold' is missing.");
                return Collections.emptyList();
            }

            if (productsSold.getBuckets() == null || productsSold.getBuckets().isEmpty()) {
                log.warn("No buckets found in 'products_sold' aggregation.");
                return Collections.emptyList();
            }

            List<MonthlyReportProductOverview> distributionList = new ArrayList<>();

            for (Terms.Bucket bucket : productsSold.getBuckets()) {
                MonthlyReportProductOverview distribution = new MonthlyReportProductOverview();
                distribution.setProductId(bucket.getKeyAsNumber().longValue());


                Sum totalQuantityAgg = bucket.getAggregations().get("total_quantity");
                double totalQuantity = 0.0;
                if (totalQuantityAgg != null) {
                    totalQuantity = totalQuantityAgg.getValue();
                    distribution.setCount(totalQuantity);
                } else {
                    log.warn("Sub-aggregation 'total_quantity' is missing for productId: {}", bucket.getKey());
                }

                // Tính toán tỷ lệ phần trăm
                if (totalProductsSold > 0) {
                    distribution.setPercentage((totalQuantity / totalProductsSold) * 100);
                } else {
                    distribution.setPercentage(0.0);
                }

                // Lấy thông tin chi tiết sản phẩm từ sub-aggregation "product_details"
                TopHits topHits = bucket.getAggregations().get("product_details");
                if (topHits != null && topHits.getHits().getHits().length > 0) {
                    Map<String, Object> sourceAsMap = topHits.getHits().getHits()[0].getSourceAsMap();
                    String title = (String) sourceAsMap.get("title");
                    distribution.setTitle(title != null ? title : "Unknown");
                } else {
                    distribution.setTitle("Unknown");
                }

                distributionList.add(distribution);
            }

            return distributionList;

        } catch (IOException e) {

            log.error("Error executing Elasticsearch query", e);
            return Collections.emptyList();
        }
    }

    public List<OrderShopStatusOverview> getOrderShopStatusOverviews(Long shopId) {
        List<OrderShopStatusOverview> orderShopStatusOverviews = new ArrayList<>();

        SearchRequest searchRequest = new SearchRequest("order-shop-document");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("shopId", shopId));

        searchSourceBuilder.query(boolQuery);

        TermsAggregationBuilder orderStatusAgg = AggregationBuilders.terms("statuses")
                .field("status")
                .size(100)
                .order(BucketOrder.count(false));

        orderStatusAgg.subAggregation(AggregationBuilders.count("total_orders").field("status"));

        searchSourceBuilder.aggregation(orderStatusAgg);

        searchSourceBuilder.aggregation(AggregationBuilders.count("overall_total_orders").field("status"));

        searchRequest.source(searchSourceBuilder);

        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

            ValueCount overallTotalOrdersAgg = searchResponse.getAggregations().get("overall_total_orders");
            long overallTotalOrders = 0;
            if(overallTotalOrdersAgg != null) {
                overallTotalOrders = overallTotalOrdersAgg.getValue();
            }else{
                log.warn("Aggregation 'overall_total_orders' is missing.");
            }

            Terms statuses = searchResponse.getAggregations().get("statuses");
            if(statuses == null) {
                log.warn("Aggregation 'statuses' is missing.");
                return Collections.emptyList();
            }
            if (statuses.getBuckets() == null || statuses.getBuckets().isEmpty()) {
                log.warn("No buckets found in 'statuses' aggregation.");
                return Collections.emptyList();
            }

            for(Terms.Bucket bucket : statuses.getBuckets()) {
                OrderShopStatusOverview orderShopStatusOverview = new OrderShopStatusOverview();
                orderShopStatusOverview.setStatus(bucket.getKeyAsString());
                ValueCount valueCount = bucket.getAggregations().get("total_orders");
                orderShopStatusOverview.setValue(valueCount.getValue());

                if(overallTotalOrders > 0){
                    orderShopStatusOverview.setPercentage((double) valueCount.getValue()/overallTotalOrders * 100);
                }else{
                    orderShopStatusOverview.setPercentage(0.0);
                }
                orderShopStatusOverviews.add(orderShopStatusOverview);
            }
            return orderShopStatusOverviews;
        }catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public OrderShopFeeOverview getOrderShopFeeOverview(Long shopId) {
        OrderShopFeeOverview orderShopFeeOverview = new OrderShopFeeOverview();
        String indexName = "order-shop-document";

        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("shopId", shopId))
                .must(QueryBuilders.termQuery("status", "GH_THANH_CONG"))
                .must(QueryBuilders.rangeQuery("updatedAt")
                        .gte("now/M")
                        .lte("now"));

        searchSourceBuilder.query(boolQuery);

        searchSourceBuilder.aggregation(AggregationBuilders.sum("total_shipping_free").field("shopShippingPrice"));
        searchSourceBuilder.aggregation(AggregationBuilders.sum("total_discount_fee").field("shopDiscount"));
        searchSourceBuilder.aggregation(AggregationBuilders.sum("total_service_free").field("serviceFee"));
        searchSourceBuilder.aggregation(AggregationBuilders.sum("total_amount").field("totalAmount"));

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            Sum shippingFreeAgg = searchResponse.getAggregations().get("total_shipping_free");
            Sum discountFeeAgg = searchResponse.getAggregations().get("total_discount_fee");
            Sum serviceFreeAgg = searchResponse.getAggregations().get("total_service_free");
            Sum totalAmountAgg = searchResponse.getAggregations().get("total_amount");

            orderShopFeeOverview.setTotalShippingFree(shippingFreeAgg != null ? shippingFreeAgg.getValue() : 0.0);
            orderShopFeeOverview.setTotalDiscountFee(discountFeeAgg != null ? discountFeeAgg.getValue() : 0.0);
            orderShopFeeOverview.setTotalServiceFree(serviceFreeAgg != null ? serviceFreeAgg.getValue() : 0.0);
            orderShopFeeOverview.setTotalAmount(totalAmountAgg != null ? totalAmountAgg.getValue() : 0.0);
            return orderShopFeeOverview;
        } catch (IOException e) {

            log.error("Error executing Elasticsearch query", e);
            return orderShopFeeOverview;
        }
    }


    //helper function
    private MonthlyRevenueOverview createEmptyResponse() {
        return MonthlyRevenueOverview.builder()
                .totalRevenue(BigDecimal.ZERO)
                .totalCost(BigDecimal.ZERO)
                .totalProfit(BigDecimal.ZERO)
                .revenuePercentageChange("0%")
                .costPercentageChange("0%")
                .profitPercentageChange("0%")
                .build();
    }

    private String calculatePercentageChange(double previous, double current) {
        if (previous == 0) {
            return current == 0 ? "0%" : "∞%";
        }
        double percentageChange = ((current - previous) / previous) * 100;
        return String.format("%.2f%%", percentageChange);
    }

    public Long countOrderByStatus(String status, Long shopId){
        String indexName = "order-shop-document";
        SearchRequest searchRequest = new SearchRequest(indexName);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("status", status))
                .must(QueryBuilders.matchQuery("shopId", shopId));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .size(0);

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse.getHits().getTotalHits().value;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ElasticsearchCustomException("failed to connect: "  +e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw new ElasticsearchCustomException("unexpected errror: "  +e.getMessage());
        }
    }

    public Long countProductByStatus(String status, Long shopId){
        String indexName = "product-document";
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("shopId",shopId));

        if(status.equals(ProductStatus.LOCKED.name())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("isAccepted",false));
        }
        if(status.equals(ProductStatus.LOW_STOCK.name())){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("stockQuantity").lte(5));
        }

        searchSourceBuilder.query(boolQueryBuilder).size(0);

        searchRequest.source(searchSourceBuilder);
        try{
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            return searchResponse.getHits().getTotalHits().value;
        }catch (Exception e){
            throw new ElasticsearchCustomException("unexcepted error: "  +e.getMessage());
        }
    }
}

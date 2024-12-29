package elk_service.elk.order.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "order-shop-document")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShopDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String orderCode;
    @Field(type = FieldType.Keyword)
    private String orderShopCode;
    @Field(type = FieldType.Keyword)
    private String status;
    @Field(type = FieldType.Double)
    private BigDecimal shopShippingPrice;
    @Field(type = FieldType.Double)
    private BigDecimal shopDiscount;
    @Field(type = FieldType.Double)
    private BigDecimal serviceFee;
    @Field(type = FieldType.Double)
    private BigDecimal totalAmount;
    @Field(type = FieldType.Keyword)
    private String deliveryMethod;
    @Field(type = FieldType.Keyword)
    private String paymentMethod;
    @Field(type = FieldType.Boolean)
    private  boolean isPayment;
    @Field(type = FieldType.Keyword)
    private Long shopId;
    @Field(type = FieldType.Date)
    private Date updatedAt;
    @Field(type = FieldType.Keyword)
    private Long userId;
    @Field(type = FieldType.Long)
    private List<Long> itemIds;
}

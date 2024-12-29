package elk_service.elk.order.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import elk_service.elk.product.entites.ProductDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "order-item-document")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDocument {
    @Id
    private Long id; // -> variantId
    @Field(type = FieldType.Integer)
    private Integer quantity;
    @Field(type = FieldType.Double)
    private BigDecimal price;
    @Field(type = FieldType.Keyword)
    private Long orderId;
    @Field(type = FieldType.Keyword)
    private Long userId;
    @Field(type = FieldType.Date)
    private Date orderDate;
    @Field(type = FieldType.Keyword)
    private Long productId;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String url;
    @Field(type = FieldType.Keyword)
    private String code;
    @Field(type = FieldType.Keyword)
    private Long shopId;
}

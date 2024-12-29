package elk_service.elk.product.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "product-document")
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "__type")
public class ProductDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String slug;
    @Field(type = FieldType.Double)
    private BigDecimal minPrice;
    @Field(type = FieldType.Double)
    private BigDecimal maxPrice;
    @Field(type = FieldType.Keyword)
    private String productCode;
    @Field(type = FieldType.Double)
    private Double rating;
    @Field(type = FieldType.Text)
    @JsonProperty("state")
    private String productState;
    @Field(type = FieldType.Keyword)
    private String shopCode;
    @Field(type = FieldType.Long)
    private Long shopId;
    @Field(type = FieldType.Long)
    private List<Long> shopCategoryIds;
    @Field(type = FieldType.Text)
    private String productUrl;
    @Field(type = FieldType.Date)
    private Date updatedAt;
    @Field(type = FieldType.Boolean)
    private Boolean isDeleted;
    @Field(type = FieldType.Boolean)
    private Boolean isAccepted;
    @Field(type = FieldType.Boolean)
    private Boolean isRejected;
    @Field(type = FieldType.Boolean)
    private Boolean isHidden;
    @Field(type = FieldType.Object)
    private ProductDocument.BrandDocument brandDocument;
    @Field(type = FieldType.Long)
    private Long stockQuantity;

    @Field(type = FieldType.Object)
    private ProductDocument.CategoryDocument categoryDocument;

    public static class BrandDocument{
        @Field(type = FieldType.Keyword)
        private Long brandId;

        @Field(type = FieldType.Text)
        private String brandName;

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }
    }

    public static class CategoryDocument{
        @Field(type = FieldType.Keyword)
        private Long categoryId;

        @Field(type = FieldType.Text)
        private String categoryName;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    }

    @Field(type = FieldType.Nested)
    private List<ProductDocument.AttributeDocument> attributes;
    public static class AttributeDocument{
        @Field(type = FieldType.Keyword)
        private Long attributeId;

        @Field(type = FieldType.Text)
        private String attributeName;

        @Field(type = FieldType.Text)
        private String attributeValue;

        public Long getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(Long attributeId) {
            this.attributeId = attributeId;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeValue() {
            return attributeValue;
        }

        public void setAttributeValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }
    }

    @Field(type = FieldType.Double)
    private Double score;
}

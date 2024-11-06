package com.example.ecommerce.entities.elastic;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.ProductState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "product-document")
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
    private ProductState productState;
    @Field(type = FieldType.Keyword)
    @JsonProperty("brand_id")
    private Long brandId;
    @Transient
    private Brand brand;
    @Field(type = FieldType.Nested)
    private List<ProductAttributeValue> attributeValues;
    @Field(type = FieldType.Nested)
    private List<Variant> variants;
    @Field(type = FieldType.Keyword)
    private Long categoryIds;
    @Field(type = FieldType.Keyword)
    private List<Long> shopCategoryIds;
    @Field(type = FieldType.Text)
    private String productUrl;


}

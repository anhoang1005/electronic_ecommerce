package com.example.ecommerce.entities.elastic;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.ProductState;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "product-document")
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
    private ProductState productState;
    @Field(type = FieldType.Keyword)
    @JsonProperty("brand_id")
    private Long brandId;
    @Transient
    private Brand brand;
    @JsonProperty("shop_id")
    private Long shopId;
    @Transient
    private Shop shop;
    @Field(type = FieldType.Keyword)
    private Long categoryIds;
    @Field(type = FieldType.Keyword)
    private List<Long> shopCategoryIds;
    @Field(type = FieldType.Text)
    private String productUrl;
    @Field(type = FieldType.Date)
    private Date updatedAt;
}

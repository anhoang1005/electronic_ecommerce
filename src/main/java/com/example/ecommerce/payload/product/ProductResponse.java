package com.example.ecommerce.payload.product;

import com.example.ecommerce.entities.Attribute;
import com.example.ecommerce.entities.AttributeValue;
import com.example.ecommerce.entities.ProductAttributeValue;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long productId;
    private String productTitle;
    private String productDescription;
    private String breadcrumbs;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> listImg;
    private Double rating;
    private Integer countComments;
    private List<VariantResponse> variant = new ArrayList<>();
    private Long favoritesCount;
    private Long brandId;
    private Long categoryId;
    private String brandName;
    private String productSpecifications;
    private String productSlug;
}

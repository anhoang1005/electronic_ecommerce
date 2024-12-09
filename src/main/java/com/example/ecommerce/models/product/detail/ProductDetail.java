package com.example.ecommerce.models.product.detail;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {
    private Long productId;
    private String title;
    private String productSlug;
    private String description;
    private String breadcrumbs;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> listImg;
    private ProductBrand productBrand;
    private ProductCategory productCategory;
    private String shopCode;
    private Double rating;
    private Integer countComments;
    private List<VariantDetail> productVariant;
    private Long favoritesCount;
    private Map<String, String> productAttribute;


    @Getter
    @Setter
    @Builder
    public static class ProductBrand{
        private Long id;
        private String brand;
    }

    @Getter
    @Setter
    @Builder
    public static class ProductCategory{
        private Long id;
        private String category;
    }
}

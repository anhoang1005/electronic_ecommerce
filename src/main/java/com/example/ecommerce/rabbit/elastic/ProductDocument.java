package com.example.ecommerce.rabbit.elastic;

import com.example.ecommerce.entities.Product;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDocument implements Serializable {

    private Long id;
    private String title;
    private String slug;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String productCode;
    private Double rating;
    private Product.ProductState productState;
    private String shopCode;
    private List<Long> shopCategoryIds;
    private String productUrl;
    private Date updatedAt;
    private Boolean isDeleted;
    private Boolean isAccepted;
    private Boolean isRejected;
    private Boolean isHidden;
    private Long shopId;
    private ProductDocument.BrandDocument brandDocument;
    private ProductDocument.CategoryDocument categoryDocument;
    private List<ProductDocument.AttributeDocument> attributes;

    @Getter
    @Setter
    public static class BrandDocument{
        private Long brandId;
        private String brandName;
    }

    @Getter
    @Setter
    public static class CategoryDocument{
        private Long categoryId;
        private String categoryName;

    }

    @Getter
    @Setter
    public static class AttributeDocument{
        private Long attributeId;
        private String attributeName;
        private String attributeValue;
    }

}

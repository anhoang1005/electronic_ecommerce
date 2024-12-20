package com.example.ecommerce.payload.product;

import com.example.ecommerce.entities.Attribute;
import com.example.ecommerce.entities.AttributeValue;
import com.example.ecommerce.entities.ProductAttributeValue;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.entities.enums.ProductState;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductResponse {
    private Long productId;
    private String productTitle;
    private String productDescription;
    private String breadcrumbs;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double rating;
    private Integer countComments;
    private List<VariantResponse> variant = new ArrayList<>();
    private Long favoritesCount;
    private String productSpecifications;
    private String productSlug;
    private String productImageUrl;
    private String productCode;
    private List<Long> variantIds = new ArrayList<>();
    private List<Long> imageIds = new ArrayList<>();
    private ProductState productState;
    private String shopCode;
    private List<Long> shopCategoryIds;
    private Date updatedAt;
    private Boolean isDeleted;
    private Boolean isAccepted;
    private Long shopId;
    private Boolean isRejected;
    private Boolean isHidden;
    private ProductResponse.BrandDocument brandDocument;
    private ProductResponse.CategoryDocument categoryDocument;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class BrandDocument{
        private Long brandId;
        private String brandName;
        public String getBrandOfProductName() {
            return brandName;
        }
        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }
        public Long getBrandOfProductId() {
            return brandId;
        }
        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CategoryDocument{
        private Long categoryId;
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProductResponse.AttributeDocument> attributes;
    public static class AttributeDocument{
        private Long attributeId;
        private String attributeName;
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

}

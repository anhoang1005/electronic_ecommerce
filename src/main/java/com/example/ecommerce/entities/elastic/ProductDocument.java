package com.example.ecommerce.entities.elastic;

import com.example.ecommerce.entities.enums.ProductState;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

    private Long id;
    private String title;
    private String slug;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String productCode;
    private Double rating;
    private ProductState productState;
    private String shopCode;
    private List<Long> shopCategoryIds;
    private String productUrl;
    private Date updatedAt;
    private Boolean isDeleted;
    private Boolean isAccepted;
    private Long shopId;
    private Boolean isRejected;
    private Boolean isHidden;
    private ProductDocument.BrandDocument brandDocument;
    private ProductDocument.CategoryDocument categoryDocument;
    public static class BrandDocument{
        private Long brandId;
        private String brandName;

        public String getBrandName() {
            return brandName;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }
    }
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
    private List<ProductDocument.AttributeDocument> attributes;
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

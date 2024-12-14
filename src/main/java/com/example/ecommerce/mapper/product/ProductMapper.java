package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductAttributeValue;
import com.example.ecommerce.entities.ShopCategoryProduct;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.payload.product.ProductResponse;
import com.example.ecommerce.utils.GlobalUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static ProductDocument mapperProductToDocument(Product product) {
        ProductDocument productDocument = new ProductDocument();
        productDocument.setId(product.getId());
        productDocument.setProductCode(product.getProductCode());
        productDocument.setTitle(product.getTitle());
        productDocument.setSlug(product.getProductSlug());
        productDocument.setRating(product.getRating());
        productDocument.setMaxPrice(product.getMaxInitialPrice());
        productDocument.setMinPrice(product.getMinInitialPrice());
        productDocument.setUpdatedAt(Timestamp.from(Instant.now()));
        productDocument.setProductState(product.getProductState());

        String url = null;
        if(product.getListImageOfProduct()!=null){
            url = String.valueOf(product.getListImageOfProduct()
                    .stream().filter(image -> image.getIsMainImage())
                    .map(image -> image.getImageUrl()).findFirst().orElse(GlobalUtils.DEFAULT_URL));
        }
        productDocument.setProductUrl(url);
        List<ShopCategoryProduct> shopCategoryProducts = product.getShopCategoryProducts();
        List<Long> shopCategoryIds = new ArrayList<>();
        shopCategoryProducts.forEach(shopCategoryProduct -> {
            shopCategoryIds.add(shopCategoryProduct.getShopCategory().getId());
        });
        productDocument.setShopCategoryIds(shopCategoryIds);
        productDocument.setIsDeleted(product.getIsDeleted());
        productDocument.setIsAccepted(product.getIsAccepted());
        productDocument.setIsRejected(product.getIsRejected());
        productDocument.setIsHidden(product.getIsHidden());

        ProductDocument.CategoryDocument categoryDocument = new ProductDocument.CategoryDocument();
        categoryDocument.setCategoryId(product.getCategory().getId());
        categoryDocument.setCategoryName(product.getCategory().getCategoryName());
        productDocument.setCategoryDocument(categoryDocument);

        ProductDocument.BrandDocument brandDocument = new ProductDocument.BrandDocument();
        brandDocument.setBrandId(product.getBrand().getId());
        brandDocument.setBrandName(product.getBrand().getBrandName());
        productDocument.setBrandDocument(brandDocument);

        List<ProductDocument.AttributeDocument> attributeDocuments = new ArrayList<>();
        List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
        for(ProductAttributeValue productAttributeValue : productAttributeValues){
            ProductDocument.AttributeDocument attributeDocument = new ProductDocument.AttributeDocument();
            attributeDocument.setAttributeId(productAttributeValue.getId());
            attributeDocument.setAttributeName(productAttributeValue.getAttribute().getAttributeName());
            attributeDocument.setAttributeValue(productAttributeValue.getAttributeValue().getValue());
            attributeDocuments.add(attributeDocument);
        }

        productDocument.setAttributes(attributeDocuments);
        return productDocument;
    }
    public static ProductResponse mapperProductToResponse(Product product){
        String url = null;
        if(product.getListImageOfProduct()!=null){
            url = String.valueOf(product.getListImageOfProduct()
                    .stream().filter(image -> image.getIsMainImage())
                    .map(image -> image.getImageUrl()).findFirst().orElse(GlobalUtils.DEFAULT_URL));
        }
        return ProductResponse
                .builder()
                .productId(product.getId())
                .productSlug(product.getProductSlug())
                .productSpecifications(product.getProductSpecifications())
                .productDescription(product.getDescription())
                .productSlug(product.getProductSlug())
                .productTitle(product.getTitle())
                .breadcrumbs(product.getBreadcrumb())
                .maxPrice(product.getMaxInitialPrice())
                .minPrice(product.getMinInitialPrice())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .productImageUrl(url)
                .countComments(product.getReview())
                .favoritesCount(product.getFavorite())
                .productCode(product.getProductCode())
                .build();
    }

}

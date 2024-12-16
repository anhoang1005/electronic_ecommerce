package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.payload.product.ProductResponse;
import com.example.ecommerce.payload.product.VariantAttributeValueResponse;
import com.example.ecommerce.payload.product.VariantResponse;
import com.example.ecommerce.utils.GlobalUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

        productDocument.setProductUrl(handleGetImgProduct(product));
        productDocument.setShopCategoryIds(handleGetListShopCategories(product));

        productDocument.setIsDeleted(product.getIsDeleted());
        productDocument.setIsAccepted(product.getIsAccepted());
        productDocument.setIsRejected(product.getIsRejected());
        productDocument.setIsHidden(product.getIsHidden());
        productDocument.setShopCode(product.getShop().getShopCode());
        productDocument.setShopId(product.getShop().getId());

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
        ProductResponse.CategoryDocument categoryDocument = new ProductResponse.CategoryDocument();
        categoryDocument.setCategoryId(product.getCategory().getId());
        categoryDocument.setCategoryName(product.getCategory().getCategoryName());

        ProductResponse.BrandDocument brandDocument = new ProductResponse.BrandDocument();
        brandDocument.setBrandId(product.getBrand().getId());
        brandDocument.setBrandName(product.getBrand().getBrandName());

        List<ProductResponse.AttributeDocument> attributeDocuments = new ArrayList<>();
        List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
        for(ProductAttributeValue productAttributeValue : productAttributeValues){
            ProductResponse.AttributeDocument attributeDocument = new ProductResponse.AttributeDocument();
            attributeDocument.setAttributeId(productAttributeValue.getId());
            attributeDocument.setAttributeName(productAttributeValue.getAttribute().getAttributeName());
            attributeDocument.setAttributeValue(productAttributeValue.getAttributeValue().getValue());

            attributeDocuments.add(attributeDocument);
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
                .productImageUrl(handleGetImgProduct(product))
                .countComments(product.getReview())
                .favoritesCount(product.getFavorite())
                .productCode(product.getProductCode())
                .isRejected(product.getIsRejected())
                .isAccepted(product.getIsAccepted())
                .isHidden(product.getIsHidden())
                .isDeleted(product.getIsDeleted())
                .shopId(product.getShop().getId())
                .shopCode(product.getShop().getShopCode())
                .shopCategoryIds(handleGetListShopCategories(product))
                .categoryDocument(categoryDocument)
                .brandDocument(brandDocument)
                .attributes(attributeDocuments)
                .variantIds(StringFormatUtils.ConvertStringToList(product.getVariantIds()))
                .productState(product.getProductState())
                .rating(product.getRating() == null ? 0 : product.getRating())
                .countComments(product.getReview() == null ? 0 : product.getReview())
                .updatedAt(Timestamp.valueOf(product.getModifiedAt()))
                .variant(mapperVariantToVariantResponses(product))
                .build();
    }

    public static String handleGetImgProduct(Product product){
        String url = null;
        if(product.getListImageOfProduct()!=null){
            url = String.valueOf(product.getListImageOfProduct()
                    .stream().filter(image -> image.getIsMainImage())
                    .map(image -> image.getImageUrl()).findFirst().orElse(GlobalUtils.DEFAULT_URL));
        }
        return url;
    }
    public static List<Long> handleGetListShopCategories(Product product){
        List<ShopCategoryProduct> shopCategoryProducts = product.getShopCategoryProducts();
        List<Long> shopCategoryIds = new ArrayList<>();
        shopCategoryProducts.forEach(shopCategoryProduct -> {
            shopCategoryIds.add(shopCategoryProduct.getShopCategory().getId());
        });
        return shopCategoryIds;
    }
        public static List<VariantResponse> mapperVariantToVariantResponses(Product product){
        List<VariantResponse> responses = new ArrayList<>();
        for(Variant variant : product.getVariants()){
            VariantResponse variantResponse = new VariantResponse();
            variantResponse.setVariantId(variant.getId());
            variantResponse.setPrice(variant.getPrice());
            variantResponse.setQuantity(variant.getQuantity());
            List<VariantAttributeValueResponse> variantAttributeValueResponses = new ArrayList<>();
            for(VariantAttributeValue variantAttributeValue : variant.getVariantAttributeValueList()){
                VariantAttributeValueResponse variantAttributeValueResponse = new VariantAttributeValueResponse();
                variantAttributeValueResponse.setAttributeId(variantAttributeValueResponse.getAttributeId());
                variantAttributeValueResponse.setAttributeName(variantAttributeValueResponse.getAttributeName());
                variantAttributeValueResponse.setAttributeValue(variantAttributeValue.getAttributeValue().getValue());
                variantAttributeValueResponses.add(variantAttributeValueResponse);
            }
            variantResponse.setAttributeValues(variantAttributeValueResponses);
            responses.add(variantResponse);
        }
        return responses;
    }

}

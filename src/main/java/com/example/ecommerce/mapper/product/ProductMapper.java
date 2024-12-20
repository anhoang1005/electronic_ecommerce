package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.models.product.card.ProductCard;
import com.example.ecommerce.models.product.detail.ProductDetail;
import com.example.ecommerce.models.product.detail.VariantDetail;
import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.payload.product.ProductResponse;
import com.example.ecommerce.payload.product.VariantAttributeValueResponse;
import com.example.ecommerce.payload.product.VariantResponse;
import com.example.ecommerce.utils.GlobalUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        productDocument.setShopCode(product.getShopOfProduct().getShopCode());
        productDocument.setShopId(product.getShopOfProduct().getId());

        ProductDocument.CategoryDocument categoryDocument = new ProductDocument.CategoryDocument();
        categoryDocument.setCategoryId(product.getCategory().getId());
        categoryDocument.setCategoryName(product.getCategory().getCategoryName());
        productDocument.setCategoryDocument(categoryDocument);

        ProductDocument.BrandDocument brandDocument = new ProductDocument.BrandDocument();
        if(!ObjectUtils.isEmpty(product.getBrandOfProduct())){
            brandDocument.setBrandId(product.getBrandOfProduct().getId());
            brandDocument.setBrandName(product.getBrandOfProduct().getBrandName());
            productDocument.setBrandDocument(brandDocument);
        }

        List<ProductDocument.AttributeDocument> attributeDocuments = new ArrayList<>();
        if(!ObjectUtils.isEmpty(product.getProductAttributeValues())){
            List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
            for(ProductAttributeValue productAttributeValue : productAttributeValues){
                ProductDocument.AttributeDocument attributeDocument = new ProductDocument.AttributeDocument();
                attributeDocument.setAttributeId(productAttributeValue.getId());
                attributeDocument.setAttributeName(productAttributeValue.getAttribute().getAttributeName());
                attributeDocument.setAttributeValue(productAttributeValue.getAttributeValue().getValue());
                attributeDocuments.add(attributeDocument);
            }
        }

        productDocument.setAttributes(attributeDocuments);
        return productDocument;
    }
//    public ProductDetail entityToProductDetail(Product product){
//        List<ProductImage> listImage = product.getListImageOfProduct();
//        List<String> imageString = new ArrayList<>();
//        for(ProductImage image : listImage){
//            imageString.add(image.getImageUrl());
//        }
//        Brand brand = product.getBrandOfProductOfProduct();
//        Category category = product.getCategory();
//        return ProductDetail.builder()
//                .productId(product.getId())
//                .title(product.getTitle())
//                .productSlug(product.getProductSlug())
//                .description(product.getDescription())
//                .breadcrumbs(product.getBreadcrumb()!=null ? product.getBreadcrumb() : null)
//                .minPrice(product.getMinInitialPrice())
//                .maxPrice(product.getMaxInitialPrice())
//                .listImg(imageString)
//                .rating(4.6)
//                .countComments(1000)
//                .variant(product.getVariants().stream()
//                        .map(this::entityToVariantDetail).collect(Collectors.toList()))
//                .favoritesCount(1000L)
//                .shopCode(product.getShopOfProduct().getShopCode())
//                .productBrand(ProductDetail.ProductBrand.builder()
//                        .id(brand.getId()).brand(brand.getBrandName())
//                        .build())
//                .productCategory(ProductDetail.ProductCategory.builder()
//                        .id(category.getId()).category(category.getCategoryName())
//                        .build())
//                .build();
//    }

    public static ProductResponse mapperProductToResponse(Product product){
        ProductResponse.CategoryDocument categoryDocument = new ProductResponse.CategoryDocument();
        categoryDocument.setCategoryId(product.getCategory().getId());
        categoryDocument.setCategoryName(product.getCategory().getCategoryName());

        ProductResponse.BrandDocument brandDocument = new ProductResponse.BrandDocument();
        if(!ObjectUtils.isEmpty(product.getBrandOfProduct())){
            brandDocument.setBrandId(product.getBrandOfProduct().getId());
            brandDocument.setBrandName(product.getBrandOfProduct().getBrandName());
        }

        List<ProductResponse.AttributeDocument> attributeDocuments = new ArrayList<>();
        if(!ObjectUtils.isEmpty(product.getProductAttributeValues())){
            List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
            for(ProductAttributeValue productAttributeValue : productAttributeValues){
                ProductResponse.AttributeDocument attributeDocument = new ProductResponse.AttributeDocument();
                attributeDocument.setAttributeId(productAttributeValue.getId());
                attributeDocument.setAttributeName(productAttributeValue.getAttribute().getAttributeName());
                attributeDocument.setAttributeValue(productAttributeValue.getAttributeValue().getValue());

                attributeDocuments.add(attributeDocument);
            }
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
                .shopId(product.getShopOfProduct().getId())
                .shopCode(product.getShopOfProduct().getShopCode())
                .shopCategoryIds(handleGetListShopCategories(product))
                .categoryDocument(categoryDocument)
                .brandDocument(brandDocument != null && brandDocument.getBrandOfProductId() == null && brandDocument.getBrandOfProductName() == null ? null : brandDocument)
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
                variantAttributeValueResponse.setAttributeId(variantAttributeValue.getAttribute().getId());
                variantAttributeValueResponse.setAttributeName(variantAttributeValue.getAttribute().getAttributeName());
                variantAttributeValueResponse.setAttributeValue(variantAttributeValue.getAttributeValue().getValue());
                variantAttributeValueResponses.add(variantAttributeValueResponse);
            }
            variantResponse.setAttributeValues(variantAttributeValueResponses);
            responses.add(variantResponse);
        }
        return responses;
    }
    public ProductCard entityToProductCard(Product product){
        List<ProductImage> imageList = product.getListImageOfProduct();
        ProductImage image = imageList.stream()
                .filter(ProductImage::getIsMainImage)
                .findFirst()
                .orElse(null);
        return ProductCard.builder()
                .productId(product.getId())
                .imageUrl(image!=null ? image.getImageUrl() : null)
                .productSlug(product.getProductSlug())
                .productCode(product.getProductCode())
                .productTitle(product.getTitle())
                .minPrice(product.getMinInitialPrice())
                .maxPrice(product.getMaxInitialPrice())
                .breadcrumbs(product.getBreadcrumb()!=null ? product.getBreadcrumb() : null)
                .rating(4.6)
                .soldCount(1000)
                .brandId(product.getBrandOfProduct().getId())
                .build();
    }

}

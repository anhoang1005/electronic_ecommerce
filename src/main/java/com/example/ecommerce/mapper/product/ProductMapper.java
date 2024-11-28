package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ShopCategoryProduct;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.payload.product.ProductResponse;
import com.example.ecommerce.utils.GlobalUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDocument mapperProductToDocument(Product product) {
        String url = GlobalUtils.DEFAULT_URL;
//        if(product.getListImageOfProduct()!=null){
//            url = String.valueOf(product.getListImageOfProduct()
//                    .stream().filter(image -> image.getIsMainImage())
//                    .map(image -> image.getImageUrl()).findFirst());
//        }
        List<ShopCategoryProduct> shopCategoryProducts = product.getShopCategoryProducts();
        List<Long> shopCategoryIds = new ArrayList<>();
        shopCategoryProducts.forEach(shopCategoryProduct -> {
            shopCategoryIds.add(shopCategoryProduct.getShopCategory().getId());
        });
        return ProductDocument
                .builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .brandId(product.getBrand().getId())
                .title(product.getTitle())
                .slug(product.getProductSlug())
                .rating(product.getRating())
                .maxPrice(product.getMaxInitialPrice())
                .minPrice(product.getMinInitialPrice())
                .productState(product.getProductState())
                .updatedAt(Date.from(product.getModifiedAt().atZone(ZoneId.systemDefault()).toInstant()))
                .shopCategoryIds(shopCategoryIds)
                .categoryIds(product.getCategory().getId())
                .shopId(product.getShop().getId())
                .productUrl(url)
                .isDeleted(product.getIsDeleted())
                .build();
    }
    public static ProductResponse mapperProductToResponse(Product product){

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

                .countComments(product.getReview())
                .listImg(product.getListImageOfProduct().stream().map(
                        img -> img.getImageUrl()
                ).collect(Collectors.toList()))
                .favoritesCount(product.getFavorite())
                .build();
    }
}

package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ShopCategoryProduct;
import com.example.ecommerce.entities.elastic.ProductDocument;
import com.example.ecommerce.utils.GlobalUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDocument mapperProductToDocument(Product product) {
        String url = GlobalUtils.DEFAULT_URL;
//        if(product.getListImageOfProduct()!=null){
//            url = String.valueOf(product.getListImageOfProduct()
//                    .stream().filter(image -> image.getIsMainImage())
//                    .map(image -> image.getImageUrl()).findFirst());
//        }
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
                .shopCategoryIds(product.getShopCategoryProducts()
                        .stream().map(ShopCategoryProduct::getId).collect(Collectors.toList()))
                .categoryIds(product.getCategory().getId())
                .shopId(product.getShop().getId())
                .productUrl(url)
                .build();
    }
}

package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.models.product.detail.ProductDetail;
import com.example.ecommerce.models.product.detail.VariantDetail;
import com.example.ecommerce.payload.product.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDetail entityToProductDetail(Product product){
        List<ProductImage> listImage = product.getListImageOfProduct();
        List<String> imageString = new ArrayList<>();
        for(ProductImage image : listImage){
            imageString.add(image.getImageUrl());
        }
        Brand brand = product.getBrandOfProduct();
        Category category = product.getCategory();
        return ProductDetail.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .productSlug(product.getProductSlug())
                .description(product.getDescription())
                .breadcrumbs(product.getBreadcrumb()!=null ? product.getBreadcrumb() : null)
                .minPrice(product.getMinInitialPrice())
                .maxPrice(product.getMaxInitialPrice())
                .listImg(imageString)
                .rating(4.6)
                .countComments(1000)
                .productVariant(product.getVariants().stream()
                        .map(this::entityToVariantDetail).collect(Collectors.toList()))
                .favoritesCount(1000L)
                .shopCode(product.getShopOfProduct().getShopCode())
                .productBrand(ProductDetail.ProductBrand.builder()
                        .id(brand.getId()).brand(brand.getBrandName())
                        .build())
                .productCategory(ProductDetail.ProductCategory.builder()
                        .id(category.getId()).category(category.getCategoryName())
                        .build())
                .build();
    }

    public VariantDetail entityToVariantDetail(ProductVariant variant){
            return VariantDetail.builder()
                    .variantName(variant.getProductVariation())
                    .soldCount(variant.getSoldQuantity())
                    .stockCount(variant.getStockQuantity())
                    .description(variant.getSku())
                    .variantName(null)
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
                .brandId(product.getBrandOfProduct().getId())

                .listImg(product.getListImageOfProduct().stream().map(
                        ProductImage::getImageUrl
                ).collect(Collectors.toList()))
                .build();
    }
}

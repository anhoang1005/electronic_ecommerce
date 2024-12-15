package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.models.product.card.ProductCard;
import com.example.ecommerce.models.product.detail.ProductDetail;
import com.example.ecommerce.models.product.detail.VariantDetail;
import com.example.ecommerce.payload.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
            VariantAtt att = new VariantAtt("RAM", "124");
            VariantAtt att1 = new VariantAtt("ROM", "124");
            VariantAtt att2 = new VariantAtt("COLOR", "124");

            List<VariantAtt> list = List.of(att, att1, att2);
            return VariantDetail.builder()
                    .variantId(variant.getId())
                    .variantName(list)
                    .soldCount(variant.getSoldQuantity())
                    .stockCount(variant.getStockQuantity())
                    .description(variant.getSku())
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


    @Getter
    @Setter
    @AllArgsConstructor
    public static class VariantAtt{
        private String name;
        private String value;
    }
}

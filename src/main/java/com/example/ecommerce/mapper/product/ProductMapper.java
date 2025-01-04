package com.example.ecommerce.mapper.product;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.rabbit.elastic.ProductDocument;
import com.example.ecommerce.models.product.card.ProductCard;
import com.example.ecommerce.models.product.detail.ProductDetail;
import com.example.ecommerce.models.product.detail.VariantDetail;
import com.example.ecommerce.payload.product.ProductResponse;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductReviewRepository;
import com.example.ecommerce.utils.GlobalUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductMapper {
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final Random random = new Random();

    public static String handleGetImgProduct(Product product){
        String url = null;
        if(product.getListImageOfProduct()!=null){
            url = product.getListImageOfProduct()
                    .stream().filter(ProductImage::getIsMainImage)
                    .map(ProductImage::getImageUrl).findFirst().orElse(GlobalUtils.DEFAULT_URL);
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

    public ProductDocument mapperProductToDocument(Product product) {
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
        productDocument.setIsHidden(product.getIsHidden());
        productDocument.setIsRejected(product.getIsReject());
        productDocument.setShopCode(product.getShopOfProduct().getShopCode());
        productDocument.setShopId(product.getShopOfProduct().getId());

        ProductDocument.CategoryDocument categoryDocument = new ProductDocument.CategoryDocument();
        categoryDocument.setCategoryId(product.getCategory().getId());
        categoryDocument.setCategoryName(product.getCategory().getCategoryName());
        productDocument.setCategoryDocument(categoryDocument);

        ProductDocument.BrandDocument brandDocument = new ProductDocument.BrandDocument();
        brandDocument.setBrandId(product.getBrandOfProduct().getId());
        brandDocument.setBrandName(product.getBrandOfProduct().getBrandName());
        productDocument.setBrandDocument(brandDocument);

        Integer productStock = 0;
        for(ProductVariant variant : product.getVariants()){
            productStock += variant.getStockQuantity();
        }
        productDocument.setStockQuantity(Long.valueOf(productStock));
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

    public ProductDetail entityToProductDetail(Product product){
        List<ProductImage> listImage = product.getListImageOfProduct();
        List<String> imageString = new ArrayList<>();
        for(ProductImage image : listImage){
            imageString.add(image.getImageUrl());
        }
        Brand brand = product.getBrandOfProduct();
        Category category = product.getCategory();

        Double averageRating = productReviewRepository.averageRatingOfProduct(product.getId());
        Double roundedRating = (averageRating != null)
                ? Math.round(averageRating * 10.0) / 10.0
                : 0.0; // Giá trị mặc định nếu là null
        return ProductDetail.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .productSlug(product.getProductSlug())
                .description(product.getDescription())
                .breadcrumbs(product.getBreadcrumb()!=null ? product.getBreadcrumb() : null)
                .minPrice(product.getMinInitialPrice())
                .maxPrice(product.getMaxInitialPrice())
                .listImg(imageString)
                .rating(roundedRating)
                .countComments(productReviewRepository.countReviewsOfProduct(product.getId()))
                .productVariant(product.getVariants().stream()
                        .map(this::entityToVariantDetail).collect(Collectors.toList()))
                .favoritesCount(random.nextLong(0,300))
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
            List<VariantAttributeValue> valueList = variant.getVariantAttributeValueList();
            List<VariantDetail.VariantAtt> variantAttList = new ArrayList<>();
            for(VariantAttributeValue value : valueList){
                variantAttList.add(VariantDetail.VariantAtt.builder()
                        .name(value.getAttribute().getAttributeName())
                        .value(value.getAttributeValue().getValue())
                        .build());
            }
            return VariantDetail.builder()
                    .variantId(variant.getId())
                    .attribute(variantAttList)
                    .status(variant.getStatus())
                    .soldCount(variant.getSoldQuantity())
                    .stockCount(variant.getStockQuantity())
                    .description(variant.getSku())
                    .sellPrice(variant.getPrice())
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
                .brandName(product.getBrandOfProduct().getBrandName())

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
        Double averageRating = productReviewRepository.averageRatingOfProduct(product.getId());
        Double roundedRating = (averageRating != null)
                ? Math.round(averageRating * 10.0) / 10.0
                : 0.0; // Giá trị mặc định nếu là null
        return ProductCard.builder()
                .productId(product.getId())
                .imageUrl(image!=null ? image.getImageUrl() : null)
                .productSlug(product.getProductSlug())
                .productCode(product.getProductCode())
                .productTitle(product.getTitle())
                .minPrice(product.getMinInitialPrice())
                .maxPrice(product.getMaxInitialPrice())
                .breadcrumbs(product.getBreadcrumb()!=null ? product.getBreadcrumb() : null)
                .rating(roundedRating)
                .soldCount(product.getSoldCount()!=null ? product.getSoldCount() : 0)
                .brand(product.getBrandOfProduct().getBrandName())
                .category(product.getCategory().getCategoryName())
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

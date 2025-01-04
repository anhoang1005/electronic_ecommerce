package com.example.ecommerce.mapper.shop;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.models.shop.ShopDetailDto;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.ShopReviewRepository;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ShopMapper {
    private final DateTimeMapperUtils dateUtils;
    private final ShopReviewRepository shopReviewRepository;
    private final ShopRepository shopRepository;

    public ShopDetailDto entityToShopDetailDto(Shop shop) {
        Double averageRating = shopReviewRepository.averageRatingOfShop(shop.getShopCode());
        Double roundedRating = (averageRating != null)
                ? Math.round(averageRating * 10.0) / 10.0
                : 0.0; // Giá trị mặc định nếu là null
        Long followCount = shopRepository.countFollowersByShopCode(shop.getId());
        if (followCount == null) {
            followCount = 0L;
        }
        Long productQuantity = shopRepository.countProductsByShopCode(shop.getShopCode());
        if (productQuantity == null) {
            productQuantity = 0L;
        }
        return ShopDetailDto.builder()
                .shopCode(shop.getShopCode())
                .businessType(shop.getBusinessType())
                .shopName(shop.getShopName())
                .shopLogo(shop.getShopLogo())
                .description(shop.getDescription())
                .address(shop.getAddress())
                .shopHotLine(shop.getShopHotLine())
                .shopEmail(shop.getShopEmail())
                .taxCode(shop.getTaxCode())
                .identityCode(shop.getIdentityCode())
                .fullName(shop.getFullName())
                .frontIdentityCard(shop.getFrontIdentityCard())
                .backIdentityCard(shop.getBackIdentityCard())
                .rating(roundedRating)
                .reviewCount(shopReviewRepository.countReviewsOfShop(shop.getShopCode()))
                .productQuantity(productQuantity)
                .followCount(followCount)
                .waitingOrder(shop.getWaitingOrder())
                .processOrder(shop.getProcessOrder())
                .deliveringOrder(shop.getDeliveringOrder())
                .successOrder(shop.getSuccessOrder())
                .cancelOrder(shop.getCancelOrder())
                .returnOrder(shop.getReturnOrder())
                .wardId(shop.getWardId())
                .districtId(shop.getDistrictId())
                .provinceId(shop.getProvinceId())
                .createdAt(dateUtils.localDateTimeToString(shop.getCreatedAt()))
                .createdBy(shop.getCreatedBy())
                .build();
    }
}

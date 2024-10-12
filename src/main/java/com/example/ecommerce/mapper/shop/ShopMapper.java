package com.example.ecommerce.mapper.shop;

import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.models.shop.ShopDetailDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {
    @Autowired
    private DateTimeMapperUtils dateUtils;

    public ShopDetailDto entityToShopDetailDto(Shop shop) {

        return ShopDetailDto.builder()
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
                .rating(shop.getRating())
                .productQuantity(shop.getProductQuantity())
                .followCount(shop.getFollowCount())
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

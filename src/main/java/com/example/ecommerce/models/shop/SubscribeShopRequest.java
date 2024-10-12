package com.example.ecommerce.models.shop;

import com.example.ecommerce.entities.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribeShopRequest {
    private Shop.BusinessType businessType;
    private String shopName;
    //private String shopLogo;
    private String description;
    private String address;
    private String shopHotLine;
    private String shopEmail;
    private String taxCode;
    private String identityCode;
    private String fullName;
    //private String frontIdentityCard;
    //private String backIdentityCard;
    private String wardId;
    private String districtId;
    private String provinceId;
}

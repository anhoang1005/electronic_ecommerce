package com.example.ecommerce.service;

import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {
    ResponseBody shopOwnerGetTheirShopDetail();
    ResponseBody customerSubscribeShop(SubscribeShopRequest req,
                                       MultipartFile frontCard, MultipartFile backCard);
    ResponseBody shopOwnerUpdateTheirShopInfo(SubscribeShopRequest req);
    ResponseBody shopOwnerRemoveTheirShop();

    ResponseBody adminChangeActiveShop(String shopCode, Boolean active);
}

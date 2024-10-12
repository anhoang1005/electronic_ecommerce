package com.example.ecommerce.service;

import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {
    ResponseBody customerGetTheirShopDetail();
    ResponseBody customerSubscribeShop(SubscribeShopRequest req,
                                       MultipartFile frontCard, MultipartFile backCard);
    ResponseBody customerUpdateTheirShopInfo(SubscribeShopRequest req);
    ResponseBody customerRemoveTheirShop(String shopCode);

    ResponseBody adminAcceptSubscribeShop(String shopCode);
    ResponseBody adminChangeActiveShop(String shopCode, Boolean active);
}

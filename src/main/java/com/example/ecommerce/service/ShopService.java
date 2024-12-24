package com.example.ecommerce.service;

import com.example.ecommerce.models.shop.SubscribeShopRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {
    ResponseBody customerGetTheirShopDetail();

    ResponseBody customerSubscribeShop(SubscribeShopRequest req,
                                       MultipartFile frontCard, MultipartFile backCard);
    ResponseBody shopOwnerUpdateTheirShopInfo(SubscribeShopRequest req);
    ResponseBody getShopDetailByCode (String shopCode);

    ResponseBody shopOwnerRemoveTheirShop();

    ResponseBody customerRestoreTheirShop();

    ResponseBody adminChangeActiveShop(String shopCode, Boolean enable);

    ResponseBody customerFollowAndUnfollowShop(String shopCode, boolean isFollow);

    ResponseBody checkUsersFollowShop(String shopCode);

    ResponseBody getListUsersFollowShop(String shopCode, int pageNumber, int pageSize);
}

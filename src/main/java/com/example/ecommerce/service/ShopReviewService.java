package com.example.ecommerce.service;

import com.example.ecommerce.models.shop_review.ShopReviewFilter;
import com.example.ecommerce.models.shop_review.ShopReviewRequest;
import com.example.ecommerce.payload.ResponseBody;

public interface ShopReviewService {

    ResponseBody customerGetPageReviewOfShop(ShopReviewFilter filter);

    ResponseBody customerGetTheirReviewOfShop(String shopCode);

    ResponseBody customerCreateReview(ShopReviewRequest req);

    ResponseBody customerUpdateReview(ShopReviewRequest req);

    ResponseBody customerDeleteReview(String shopCode);

    ResponseBody adminRemoveReview(Long reviewId);

    ResponseBody ownerShopReplyShopReview(String userCode, String reply);
}

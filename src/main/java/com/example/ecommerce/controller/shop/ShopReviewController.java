package com.example.ecommerce.controller.shop;

import com.example.ecommerce.models.shop_review.ShopReviewFilter;
import com.example.ecommerce.models.shop_review.ShopReviewRequest;
import com.example.ecommerce.service.ShopReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class ShopReviewController {

    @Autowired
    private ShopReviewService shopReviewService;

    @GetMapping("/api/guest/shop/page-review")
    public ResponseEntity<?> customerGetPageShopReviewApi(
            @RequestBody ShopReviewFilter filter){
        return ResponseEntity.ok(shopReviewService.customerGetPageReviewOfShop(filter));
    }

    @GetMapping("/api/customer/shop/my-review")
    public ResponseEntity<?> customerGetTheirShopReviewApi(
            @RequestParam("shop_code") String shopCode){
        return ResponseEntity.ok(shopReviewService.customerGetTheirReviewOfShop(shopCode));
    }

    @PostMapping("/api/customer/shop/create-review")
    public ResponseEntity<?> customerCreateShopReviewApi(
            @RequestBody ShopReviewRequest req){
        return ResponseEntity.ok(shopReviewService.customerCreateReview(req));
    }

    @PutMapping("/api/customer/shop/update-review")
    public ResponseEntity<?> customerUpdateShopReviewApi(
            @RequestBody ShopReviewRequest req){
        return ResponseEntity.ok(shopReviewService.customerUpdateReview(req));
    }

    @DeleteMapping("//api/customer/shop/delete-review")
    public ResponseEntity<?> customerDeleteShopReviewApi(
            @RequestParam("shop_code") String shopCode){
        return ResponseEntity.ok(shopReviewService.customerDeleteReview(shopCode));
    }

    @DeleteMapping("/api/admin/shop/delete-review")
    public ResponseEntity<?> adminRemoveShopReviewApi(
            @RequestParam("review_id") Long reviewId){
        return ResponseEntity.ok(shopReviewService.adminRemoveReview(reviewId));
    }

    @PutMapping("/api/owner/shop/reply-review")
    public ResponseEntity<?> ownerReplyShopReviewApi(
            @RequestParam("user_code") String userCode,
            @RequestParam("reply_text") String reply){
        return ResponseEntity.ok(shopReviewService.ownerShopReplyShopReview(userCode, reply));
    }
}

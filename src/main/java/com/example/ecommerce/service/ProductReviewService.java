package com.example.ecommerce.service;

import com.example.ecommerce.models.product_review.ProductReviewFilter;
import com.example.ecommerce.models.product_review.ProductReviewRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface ProductReviewService {
    ResponseBody guestGetListReviewOfProduct(ProductReviewFilter filter);
    ResponseBody customerCreateProductReview(ProductReviewRequest req, MultipartFile image);
    ResponseBody customerUpdateProductReview(ProductReviewRequest req, MultipartFile image);
    ResponseBody customerDeleteProductReview(String productId);
    ResponseBody shopOwnerReplyProductReview(String reviewId, String shopReply);

    ResponseBody customerLikeProductReview(String reviewId, boolean isLike);
}

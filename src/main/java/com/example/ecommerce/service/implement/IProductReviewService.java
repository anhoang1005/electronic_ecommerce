package com.example.ecommerce.service.implement;

import com.example.ecommerce.models.product_review.ProductReviewFilter;
import com.example.ecommerce.models.product_review.ProductReviewRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductReviewRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.ProductReviewService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class IProductReviewService implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final AuthenticationUtils authUtils;


    @Override
    public ResponseBody guestGetListReviewOfProduct(ProductReviewFilter filter) {
        return null;
    }

    @Override
    public ResponseBody customerCreateProductReview(ProductReviewRequest req, MultipartFile image) {
        return null;
    }

    @Override
    public ResponseBody customerUpdateProductReview(ProductReviewRequest req, MultipartFile image) {
        return null;
    }

    @Override
    public ResponseBody customerDeleteProductReview(String productId) {
        return null;
    }

    @Override
    public ResponseBody shopOwnerReplyProductReview(String reviewId, String shopReply) {
        return null;
    }

    @Override
    public ResponseBody customerLikeProductReview(String reviewId, boolean isLike) {
        return null;
    }
}

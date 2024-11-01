package com.example.ecommerce.mapper.product_review;

import com.example.ecommerce.entities.ProductReview;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.models.product_review.ProductReviewDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductReviewMapper {
    private final DateTimeMapperUtils dateUtils;

    public ProductReviewDto entityToPRDto(ProductReview review){
        Users users = review.getUsersOfProductReview();
        return ProductReviewDto.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .textComment(review.getTextComment())
                .imageComment(review.getImageComment())
                .isShopReply(review.getIsShopReply())
                .shopReply(review.getShopReply())
                .tag(List.of(review.getTag()))
                .createdAt(dateUtils.localDateTimeToString(review.getCreatedAt()))
                .likeCount(review.getLikeCount())
                .username(users.getUsername())
                .userAvatar(users.getImageUrl())
                .build();
    }
}

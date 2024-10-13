package com.example.ecommerce.mapper.shop_review;

import com.example.ecommerce.entities.ShopReview;
import com.example.ecommerce.models.shop_review.ShopReviewDto;
import org.springframework.stereotype.Component;

@Component
public class ShopReviewMapper {
    public ShopReviewDto entityToShopReviewDto(ShopReview review){
        if(review != null){
            return ShopReviewDto.builder()
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .isReply(review.getIsReply())
                    .reply(review.getReply())
                    .active(review.getActive())
                    .build();
        } else{
            return null;
        }
    }
}

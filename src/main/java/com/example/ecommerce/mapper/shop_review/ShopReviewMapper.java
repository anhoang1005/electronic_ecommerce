package com.example.ecommerce.mapper.shop_review;

import com.example.ecommerce.entities.ShopReview;
import com.example.ecommerce.models.shop_review.ShopReviewDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import org.springframework.stereotype.Component;

@Component
public class ShopReviewMapper {

    private final DateTimeMapperUtils dateUtils;
    public ShopReviewMapper(DateTimeMapperUtils dateUtils) {
        this.dateUtils = dateUtils;
    }

    public ShopReviewDto entityToShopReviewDto(ShopReview review){
        if(review != null){
            return ShopReviewDto.builder()
                    .id(review.getId())
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .isReply(review.getIsReply())
                    .reply(review.getReply())
                    .active(review.getActive())
                    .createdAt(dateUtils.localDateTimeToString(review.getCreatedAt()))
                    .modifiedAt(dateUtils.localDateTimeToString(review.getModifiedAt()))
                    .build();
        } else{
            return null;
        }
    }
}

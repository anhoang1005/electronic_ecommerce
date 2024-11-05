package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    @Query("select pr from ProductReview pr " +
            "where pr.productOfProductReview.id = :productId " +
            "and (:rating is null or pr.rating = :rating)")
    Page<ProductReview> guestGetListReviewOfProduct(@Param("productId") Long productId,
                                                    @Param("rating") Integer rating,
                                                    Pageable pageable);


    @Query(value = "select pr from ProductReview pr " +
            "where pr.usersOfProductReview.id = :userId " +
            "and pr.productOfProductReview.id = :productId")
    Optional<ProductReview> findByProductIdAndUserId(@Param("productId") Long productId,
                                                     @Param("userId") Long userId);
}

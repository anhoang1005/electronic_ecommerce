package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    Boolean existsByUsersOfProductReview_UserCodeAndProductOfProductReview_Id(
            String userCode, Long productId);

    @Query("select r from ProductReview r " +
            "where r.id = :reviewId " +
            "and r.productOfProductReview.shopOfProduct.shopCode = :shopCode")
    Optional<ProductReview> getReviewOfShop(Long reviewId, String shopCode);

    Optional<ProductReview> findByUsersOfProductReview_IdAndProductOfProductReview_Id(Long userId, Long productId);

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

    @Query("SELECT AVG(r.rating) " +
            "FROM ProductReview r " +
            "WHERE r.productOfProductReview.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    Long countByProductOfProductReview_Id(Long productId);

    @Query("SELECT r.rating, COUNT(r) " +
            "FROM ProductReview r " +
            "WHERE r.productOfProductReview.id = :productId " +
            "GROUP BY r.rating " +
            "ORDER BY r.rating")
    List<Object[]> countReviewsByRating(@Param("productId") Long productId);

    @Query("SELECT AVG(r.rating) FROM ProductReview r " +
            "WHERE r.productOfProductReview.id = :productId")
    Double averageRatingOfProduct(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM ProductReview r " +
            "WHERE r.productOfProductReview.id = :productId")
    Long countReviewsOfProduct(@Param("productId") Long productId);


}

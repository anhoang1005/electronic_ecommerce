package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopReviewRepository extends JpaRepository<ShopReview, Long> {

    @Query(value = "select sr from ShopReview sr " +
            "where sr.usersOfShopReview.email = :email " +
            "and sr.shopOfShopReview.shopCode = :shopCode")
    Optional<ShopReview> findShopReviewByUserEmailAndShopCode(
            @Param("email") String email,
            @Param("shopCode") String shopCode);

    ShopReview findShopReviewById(Long id);

    @Query(value = "select sr from ShopReview sr " +
            "where sr.shopOfShopReview.shopCode = :shopCode " +
            "and (:rating is null or sr.rating = :rating) " +
            "and sr.active = true ")
    Page<ShopReview> customerGetListShopReviewOfShop(
            @Param("shopCode") String shopCode,
            @Param("rating") Integer rating,
            Pageable pageable);

    @Query("select sr from ShopReview sr where sr.shopOfShopReview.usersInShop.email = :ownerEmail " +
            "and sr.usersOfShopReview.userCode = :userCode")
    Optional<ShopReview> ownerFindReviewByUsersCodeAndOwnerShopEmail(@Param("ownerEmail") String ownerEmail,
                                                                     @Param("userCode") String userCode);
}

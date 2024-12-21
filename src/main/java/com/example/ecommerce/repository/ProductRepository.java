package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

//    @Transactional
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    default void findAndLockUpdateReview(Long productId, Integer oldStar, Integer newStar, String action){
//        Product product = findById(productId).orElseThrow(()-> new IllegalArgumentException("ERROR"));
//        switch (action) {
//            case "INSERT" -> {
//                product.setReviewCount(product.getReviewCount() + 1);
//                product.setStarCount(product.getStarCount() + newStar);
//            }
//            case "UPDATE" -> product.setStarCount(product.getStarCount() - oldStar + newStar);
//            case "DELETE" -> {
//                product.setReviewCount(product.getReviewCount() - 1);
//                product.setStarCount(product.getStarCount() - oldStar);
//            }
//        }
//        double rating = (product.getReviewCount() > 0) ?
//                (double) product.getStarCount() / product.getReviewCount() : 0.0;
//        product.setRating(rating);
//        save(product);
//    }

    Product findProductById(Long id);
    @Query("select p from Product p  where p.isAccepted = true and p.isHidden = false and p.productCode = :code")
    Product findProductByIdIfAcceptedAndNotHidden(@RequestParam String code);
    Product findProductByProductCode(String productCode);

//    @Query("select p from Product p  where p.isAccepted = true and p.isHidden = false and p.id = :id")
//    Product findProductByIdIfAcceptedAndNotHidden(@RequestParam Long id);

    @Query("""
        SELECT DISTINCT p FROM Product p 
        LEFT JOIN FETCH p.shopOfProduct s
        LEFT JOIN FETCH p.category c
        LEFT JOIN FETCH p.brandOfProduct b
        WHERE p.id = :id
    """)
    Product findProductWithBasicRelationships(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.listImageOfProduct WHERE p.id = :id")
    Product findProductWithImages(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.categoryProducts WHERE p.id = :id")
    Product findProductWithCategories(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.shopCategoryProducts WHERE p.id = :id")
    Product findProductWithShopCategories(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :id")
    Product findProductWithVariants(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productAttributeValues WHERE p.id = :id")
    Product findProductWithAttributes(@Param("id") Long id);
    Page<Product> findAll(Pageable pageable);
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.shopCategoryProducts WHERE p.id = :id")
    Product findProductWithCategoriesById(@Param("id") Long id);
}

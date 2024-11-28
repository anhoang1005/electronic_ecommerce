package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Product;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findProductById(Long id);
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    default void findAndLockUpdateReview(Long productId, Integer oldStar, Integer newStar, String action){
        Product product = findById(productId).orElseThrow(()-> new IllegalArgumentException("ERROR"));
        switch (action) {
            case "INSERT" -> {
                product.setReviewCount(product.getReviewCount() + 1);
                product.setStarCount(product.getStarCount() + newStar);
            }
            case "UPDATE" -> product.setStarCount(product.getStarCount() - oldStar + newStar);
            case "DELETE" -> {
                product.setReviewCount(product.getReviewCount() - 1);
                product.setStarCount(product.getStarCount() - oldStar);
            }
        }
        double rating = (product.getReviewCount() > 0) ?
                (double) product.getStarCount() / product.getReviewCount() : 0.0;
        product.setRating(rating);
        save(product);
    }
    @Query("select p from Product  p where p.id = :id and p.isAccepted =  true and p.isHidden = true")
    Product findProductByIdIfAcceptedAndNotHidden(@RequestParam Long id);

}

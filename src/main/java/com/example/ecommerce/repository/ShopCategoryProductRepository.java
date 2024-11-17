package com.example.ecommerce.repository;


import com.example.ecommerce.entities.ShopCategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopCategoryProductRepository extends JpaRepository<ShopCategoryProduct,Long> {
    ShopCategoryProduct findShopCategoryProductByProductIdAndShopCategoryId(Long productId, Long shopCategoryId);

}

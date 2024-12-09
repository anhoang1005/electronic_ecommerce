package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopCategoryRepository extends JpaRepository<ShopCategory,Long> {
    ShopCategory findShopCategoriesById(Long id);
    @Query("SELECT sc FROM ShopCategory sc WHERE sc.categoryLevel > 1 ORDER BY function('RAND')")
    ShopCategory findRandomShopCategory();

}

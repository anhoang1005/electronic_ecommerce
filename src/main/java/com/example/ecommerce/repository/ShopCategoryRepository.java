package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopCategoryRepository extends JpaRepository<ShopCategory,Long> {
    ShopCategory findShopCategoriesById(Long id);
    @Query("select sc from ShopCategory sc order by function('RAND') LIMIT 1")
    ShopCategory findRandomShopCategory();
    @Query("select sc from ShopCategory sc where sc.shopOfCategory.id = :shopId and sc.id = :id and sc.active = true")
    Optional<ShopCategory> findShopCategoriesIfActive(Long shopId,Long id);
    List<ShopCategory> findShopCategoriesByShopOfCategoryIdAndCategoryLevel(Long shopOfCategoryId, Integer categoryLevel);
    List<ShopCategory> findShopCategoriesByParentCategoryIdAndShopOfCategoryIdAndCategoryLevel(Long parentCategoryId, Long shopOfCategoryId, Integer categoryLevel);
}

package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCategoryRepository extends JpaRepository<ShopCategory,Long> {
    ShopCategory findShopCategoriesById(Long id);
    @Query("select sc from ShopCategory sc order by function('RAND') LIMIT 1")
    ShopCategory findRandomShopCategory();

        List<ShopCategory> findShopCategoriesByShopOfCategoryIdAndActiveAndCategoryLevel(Long shopOfCategoryId, Boolean active, Integer categoryLevel);
//        List<ShopCategory> findShopCategoriesByShopOfCategoryIdAndActiveAndCategoryLevelAndParentCategory(Long shopOfCategoryId, Boolean active, Integer categoryLevel, Long parentId);
}

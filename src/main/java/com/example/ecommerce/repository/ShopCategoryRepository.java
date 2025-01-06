package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCategoryRepository extends JpaRepository<ShopCategory,Long> {
    ShopCategory findShopCategoriesById(Long id);
    @Query("SELECT sc FROM ShopCategory sc WHERE sc.shopOfCategory.id = :id ORDER BY function('RAND')")
    List<ShopCategory> findRandomShopCategoriesByShop(Long id);

    @Query("select sc from ShopCategory sc " +
            "where sc.categoryName = :categoryName " +
            "and sc.shopOfCategory.shopCode = :shopCode ")
    ShopCategory findShopCategoryByCategoryNameAndShop(@Param("categoryName") String name,
                                                       @Param("shopCode") String shopCode);

}

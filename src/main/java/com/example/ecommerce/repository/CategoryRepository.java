package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findCategoryById(Long id);
    Category findCategoryByCategoryNameAndCategoryLevel(String name,Integer level);
    List<Category> findCategoriesByCategoryLevel(Integer level);
    List<Category> findCategoriesByCategoryLevelAndParentCategory(Integer level, Category parent);
}

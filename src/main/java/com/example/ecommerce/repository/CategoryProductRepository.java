package com.example.ecommerce.repository;

import com.example.ecommerce.entities.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct,Long> {
}

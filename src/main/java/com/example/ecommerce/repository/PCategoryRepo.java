package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Product_Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCategoryRepo extends JpaRepository<Product_Category,Long> {
}

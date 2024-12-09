package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<ProductVariant,Long> {
    ProductVariant findVariantByProductVariation(String productVariation);
}

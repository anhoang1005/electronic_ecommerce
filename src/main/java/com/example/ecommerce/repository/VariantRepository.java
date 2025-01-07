package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<ProductVariant,Long> {
    ProductVariant findVariantByProductVariation(String productVariation);

    @Query(value = "SELECT * FROM product_variants ORDER BY RAND() LIMIT 2", nativeQuery = true)
    List<ProductVariant> findRandom2Variant();
}

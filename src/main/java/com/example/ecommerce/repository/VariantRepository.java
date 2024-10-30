package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<Variant,Long> {
    Variant findVariantByProductVariation(String productVariation);
}

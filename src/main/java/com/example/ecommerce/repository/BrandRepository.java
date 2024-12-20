package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findBrandByBrandName(String name);
    @Query("SELECT b FROM Brand b ORDER BY FUNCTION('RAND') LIMIT 1")
    Brand findRandomBrand();
    Page<Brand> findAll(Pageable pageable);

}

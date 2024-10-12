package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Voucher_Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VProductRepo extends JpaRepository<Voucher_Products,Long> {
}

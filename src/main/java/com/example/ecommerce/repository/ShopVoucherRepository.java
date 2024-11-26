package com.example.ecommerce.repository;

import com.example.ecommerce.entities.ShopVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopVoucherRepository extends JpaRepository<ShopVoucher, Long> {
    Optional<ShopVoucher> findShopVoucherByVoucherCode(String voucherCode);
}

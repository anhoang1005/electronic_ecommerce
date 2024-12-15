package com.example.ecommerce.repository;

import com.example.ecommerce.entities.OrdersShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrderShopRepository extends JpaRepository<OrdersShop, Long> {

    @Modifying
    @Query("UPDATE Orders o SET o.ecommerceServiceFee = :fee WHERE o.id = :id")
    void updateEcommerceServiceFee(@Param("fee") BigDecimal fee,
                                   @Param("id") Long id);
}

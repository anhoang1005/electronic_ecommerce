package com.example.ecommerce.repository;

import com.example.ecommerce.entities.OrdersShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderShopRepository extends JpaRepository<OrdersShop, Long> {
}

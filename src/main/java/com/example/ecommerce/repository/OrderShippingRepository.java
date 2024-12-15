package com.example.ecommerce.repository;

import com.example.ecommerce.entities.OrderShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderShippingRepository extends JpaRepository<OrderShipping, Long> {
}

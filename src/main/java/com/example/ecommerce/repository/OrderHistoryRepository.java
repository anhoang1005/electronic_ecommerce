package com.example.ecommerce.repository;

import com.example.ecommerce.entities.OrdersHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrdersHistory, Long> {
}

package com.example.ecommerce.repository;

import com.example.ecommerce.entities.OrdersItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrdersItems,Long> {

}

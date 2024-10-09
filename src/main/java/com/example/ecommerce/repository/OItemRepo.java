package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Order_Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OItemRepo extends JpaRepository<Order_Item,Long> {
}

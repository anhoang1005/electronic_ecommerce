package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Order_Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ODetailsRepo extends JpaRepository<Order_Detail,Long> {
}

package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

    Optional<Orders> findByOrderCode(String orderCode);

    @Query(value = "select o from Orders o " +
            "where (:code is null or o.orderCode = :code) " +
            "and (:status is null or o.status = :status) " +
            "and (:payment is null or o.paymentMethod = :payment) " +
            "and (:isPayment is null or o.isPayment = :isPayment) " +
            "and ((:start is null or :end is null ) or o.createdAt between :start and :end) ")
    Page<Orders> adminGetDashboard(@Param("code") String orderCode,
                                   @Param("status") Orders.Status status,
                                   @Param("payment") String payment,
                                   @Param("isPayment") Boolean isPayment,
                                   @Param("start")LocalDateTime start,
                                   @Param("end")LocalDateTime end,
                                   Pageable pageable);
}

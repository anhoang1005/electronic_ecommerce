package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Orders;
import com.example.ecommerce.entities.OrdersShop;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderShopRepository extends JpaRepository<OrdersShop, Long> {

    @Modifying
    @Query("UPDATE Orders o SET o.ecommerceServiceFee = :fee WHERE o.id = :id")
    void updateEcommerceServiceFee(@Param("fee") BigDecimal fee,
                                   @Param("id") Long id);

    Optional<OrdersShop> findByOrderShopCode(String orderCode);

    @Query(value = "select o from OrdersShop o " +
            "where o.ordersOfOrdersShop.usersOfOrder.id = :userId " +
            "and (:orderCode is null or o.orderShopCode = :orderCode) " +
            "and (:orderStatus is null or o.orderStatus = :orderStatus) " )
    Page<OrdersShop> customerGetDashBoard(@Param("userId") Long userId,
                                          @Param("orderCode") String orderCode,
                                          @Param("orderStatus") OrdersShop.Status status,
                                          Pageable pageable);


    @Query(value = "select o from OrdersShop o " +
            "where (:orderCode is null or o.orderShopCode = :orderCode) " +
            "and (:orderStatus is null or o.orderStatus = :orderStatus) " +
            "and (:clientName is null or o.orderShippingOfShopOrder.clientName like %:clientName%) " +
            "and (:phoneNumber is null or o.orderShippingOfShopOrder.clientTelephone like %:phoneNumber%) " +
            "and (:address is null or o.orderShippingOfShopOrder.clientAddress like %:address%) " +
            "and ((:startDate is null or :endDate is null ) or o.createdAt between :startDate and :endDate) ")
    Page<OrdersShop> shopGetDashboard(@Param("orderCode") String orderCode,
                                      @Param("orderStatus") OrdersShop.Status status,
                                      @Param("clientName") String clientName,
                                      @Param("phoneNumber") String phoneNumber,
                                      @Param("address") String address,
                                      @Param("startDate")LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      Pageable pageable);

}

package com.example.ecommerce.repository;

import com.example.ecommerce.entities.OrdersItems;
import com.example.ecommerce.entities.OrdersShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrdersItems,Long> {

    @Query("SELECT oi FROM OrdersItems oi " +
            "JOIN oi.ordersShopOfOrdersItems os " +
            "JOIN os.ordersOfOrdersShop o " +
            "WHERE o.usersOfOrder.userCode = :userCode " +
            "AND oi.productId = :productId " +
            "AND os.orderStatus = :status")
    Optional<OrdersItems> findOrderItemByUserIdAndProductId(@Param("userCode") String userCode,
                                                            @Param("productId") Long productId,
                                                            @Param("status") OrdersShop.Status status);

}

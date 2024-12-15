package com.example.ecommerce.controller.order;

import com.example.ecommerce.entities.Orders;
import com.example.ecommerce.entities.OrdersShop;
import com.example.ecommerce.models.order.OrderCustomerFilter;
import com.example.ecommerce.models.order.OrderFilter;
import com.example.ecommerce.models.order.OrderShopFilter;
import com.example.ecommerce.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/admin/order/dashboard")
    public ResponseEntity<?> adminGetDashBoard(@RequestBody OrderFilter filter){
        return ResponseEntity.ok(orderService.adminGetDashBoard(filter));
    }

    @PutMapping("/api/admin/order/update")
    public ResponseEntity<?> adminUpdateOrder(@RequestParam("orderCode") String orderCode,
                                              @RequestParam("status")Orders.Status status){
        return ResponseEntity.ok(orderService.adminUpdateOrder(orderCode, status));
    }

    @PostMapping("/api/owner/order/dashboard")
    public ResponseEntity<?> shopGetDashBoard(@RequestBody OrderShopFilter filter){
        return ResponseEntity.ok(orderService.shopGetDashboard(filter));
    }

    @PutMapping("/api/owner/order/update")
    public ResponseEntity<?> shopUpdateOrder(@RequestParam("orderCode") String orderCode,
                                             @RequestParam("status")OrdersShop.Status status){
        return ResponseEntity.ok(orderService.shopUpdateOrder(orderCode, status));
    }

    @PostMapping("/api/customer/order/dashboard")
    public ResponseEntity<?> customerGetDashBoard(@RequestBody OrderCustomerFilter filter){
        return ResponseEntity.ok(orderService.customerGetTheirOrder(filter));
    }

    @PutMapping("/api/customer/order/cancel")
    public ResponseEntity<?> customerCancelDashboard(@RequestParam("orderCode") String orderCode){
        return ResponseEntity.ok(orderService.customerCancelOrder(orderCode));
    }
}

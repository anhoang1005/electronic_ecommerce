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

    @GetMapping("/api/admin/order/detail/{order_code}")
    public ResponseEntity<?> adminGetDashBoard(@PathVariable("order_code") String orderCode){
        return ResponseEntity.ok(orderService.adminGetOrderDetail(orderCode));
    }

    @PutMapping("/api/admin/order/update/{order_code}")
    public ResponseEntity<?> adminUpdateOrder(@PathVariable("order_code") String orderCode,
                                              @RequestParam("status")Orders.Status status){
        return ResponseEntity.ok(orderService.adminUpdateOrder(orderCode, status));
    }
    @PostMapping("/api/owner/order/dashboard")
    public ResponseEntity<?> shopGetDashBoard(@RequestBody OrderShopFilter filter){
        return ResponseEntity.ok(orderService.shopGetDashboard(filter));
    }

    @GetMapping("/api/owner/order/detail/{order_code}")
    public ResponseEntity<?> shopGetOrderDetail(@PathVariable("order_code") String orderCode){
        return ResponseEntity.ok(orderService.shopGetOrderDetail(orderCode));
    }

    @PutMapping("/api/owner/order/update/{order_code}")
    public ResponseEntity<?> shopUpdateOrder(@PathVariable("order_code") String orderCode,
                                             @RequestParam("status")OrdersShop.Status status){
        return ResponseEntity.ok(orderService.shopUpdateOrder(orderCode, status));
    }

    @PostMapping("/api/customer/order/dashboard")
    public ResponseEntity<?> customerGetDashBoard(@RequestBody OrderCustomerFilter filter){
        return ResponseEntity.ok(orderService.customerGetTheirOrder(filter));
    }

    @GetMapping("/api/customer/order/detail/{order_code}")
    public ResponseEntity<?> customerGetDashBoard(@PathVariable("order_code") String orderCode){
        return ResponseEntity.ok(orderService.customerGetOrderDetail(orderCode));
    }

    @PutMapping("/api/customer/order/cancel/{order_code}")
    public ResponseEntity<?> customerCancelDashboard(@PathVariable("order_code") String orderCode){
        return ResponseEntity.ok(orderService.customerCancelOrder(orderCode));
    }
}

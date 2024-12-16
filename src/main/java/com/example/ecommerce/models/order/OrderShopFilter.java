package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrderPayment;
import com.example.ecommerce.entities.OrdersShop;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderShopFilter {
    private String orderCode;
    private OrdersShop.Status orderStatus;
    private String userFullName;
    private String phoneNumber;
    private String address;
    private String sort;
    private String startDate;
    private String endDate;
    private int pageNumber;
    private int pageSize;
}

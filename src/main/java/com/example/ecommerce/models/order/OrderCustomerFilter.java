package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrdersShop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCustomerFilter {
    private String orderCode;
    private OrdersShop.Status status;
    private String sort;
    private int pageNumber;
    private int pageSize;
}

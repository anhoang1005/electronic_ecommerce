package com.example.ecommerce.models.order;

import com.example.ecommerce.entities.OrderPayment;
import com.example.ecommerce.entities.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilter {
    private String orderCode;
    private Orders.Status status;
    private OrderPayment.Method paymentMethod;
    private Boolean isPayment;
    private String startDate;
    private String endDate;
    private int pageNumber;
    private int pageSize;
}

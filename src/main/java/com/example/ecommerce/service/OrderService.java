package com.example.ecommerce.service;

import com.example.ecommerce.entities.Orders;
import com.example.ecommerce.entities.OrdersShop;
import com.example.ecommerce.models.order.OrderCustomerFilter;
import com.example.ecommerce.models.order.OrderFilter;
import com.example.ecommerce.models.order.OrderShopFilter;
import com.example.ecommerce.payload.ResponseBody;

public interface OrderService {

    ResponseBody shopUpdateOrder(String orderCode, OrdersShop.Status status);
    ResponseBody shopGetDashboard(OrderShopFilter filter);

    ResponseBody customerGetTheirOrder(OrderCustomerFilter filter);
    ResponseBody customerCancelOrder(String orderCode);

    ResponseBody adminGetDashBoard(OrderFilter filter);
    ResponseBody adminUpdateOrder(String orderCode, Orders.Status status);
}

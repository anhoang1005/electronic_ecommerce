package com.example.ecommerce.mapper.order;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.models.order.*;
import com.example.ecommerce.models.order.check_out.OrderDetailDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final DateTimeMapperUtils timeUtils;

    public OrderDto entityToOrderDto(Orders order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .totalFee(order.getTotalFee())
                .totalShipping(order.getTotalShipping())
                .totalDiscount(order.getTotalDiscount())
                .totalAmount(order.getTotalAmount())
                .ecommerceServiceFee(order.getEcommerceServiceFee())
                .deliveryMethod(order.getDeliveryMethod())
                .clientInfo(order.getClientName() + " - " + order.getClientTelephone() +
                        " - " + order.getClientAddress())
                .paymentMethod(order.getPaymentMethod())
                .isPayment(order.getIsPayment())
                .build();
    }

    public OrderDetailDto entityToOrderDetailDto(Orders order){
        List<OrderPayment> paymentList = order.getListOrderPayment();
        List<OrdersShop> ordersShopList = order.getListOrderShopOfOrders();
        return OrderDetailDto.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .totalFee(order.getTotalFee())
                .totalShipping(order.getTotalShipping())
                .totalDiscount(order.getTotalDiscount())
                .totalAmount(order.getTotalAmount())
                .ecommerceServiceFee(order.getEcommerceServiceFee())
                .deliveryMethod(order.getDeliveryMethod())
                .clientInfo(order.getClientName() + " - " + order.getClientTelephone() +
                        " - " + order.getClientAddress())
                .paymentMethod(order.getPaymentMethod())
                .isPayment(order.getIsPayment())
                .listOrderPayment(paymentList.stream().map(this::entityToPaymentDto).collect(Collectors.toList()))
                .listOrderShop(ordersShopList.stream().map(this::entityToOrderShopDto).collect(Collectors.toList()))
                .build();
    }

    public OrderShopDto entityToOrderShopDto(OrdersShop order) {
        OrderShipping shipping = order.getOrderShippingOfShopOrder();
        Shop shop = order.getShopOfOrdersShop();
        return OrderShopDto.builder()
                .id(order.getId())
                .shopCode(shop.getShopCode())
                .shopName(shop.getShopName())
                .shopLogo(shop.getShopLogo())
                .orderShopCode(order.getOrderShopCode())
                .orderStatus(order.getOrderStatus())
                .totalProduct(order.getTotalProduct())
                .shopShippingFee(order.getShopShippingFee())
                .shopDiscount(order.getShopDiscount())
                .serviceFee(order.getServiceFee())
                .shopTotalAmount(order.getShopTotalAmount())
                .ecommerceTotalAmount(order.getEcommerceTotalAmount())
                .deliveryMethod(order.getDeliveryMethod())
                .paymentMethod(order.getPaymentMethod())
                .isPayment(order.getIsPayment())
                .clientInfo(shipping.getClientName() + "-" + shipping.getClientTelephone() +
                        "-" + shipping.getClientAddress())
                .build();
    }

    public OrderShopDetail entityToOrderDetailDto(OrdersShop order) {
        OrderShipping shipping = order.getOrderShippingOfShopOrder();
        List<OrdersHistory> historyList = order.getListOrderHistoryOfOrderShop();
        List<OrdersItems> itemsList = order.getListOrdersItemsOfOrdersShop();
        return OrderShopDetail.builder()
                .id(order.getId())
                .orderShopCode(order.getOrderShopCode())
                .orderStatus(order.getOrderStatus())
                .totalProduct(order.getTotalProduct())
                .shopShippingFee(order.getShopShippingFee())
                .shopDiscount(order.getShopDiscount())
                .serviceFee(order.getServiceFee())
                .shopTotalAmount(order.getShopTotalAmount())
                .ecommerceTotalAmount(order.getEcommerceTotalAmount())
                .deliveryMethod(order.getDeliveryMethod())
                .paymentMethod(order.getPaymentMethod())
                .isPayment(order.getIsPayment())
                .shippingDto(entityToShippingDto(shipping))
                .itemDtoList(itemsList.stream().map(this::entityToItemDto).collect(Collectors.toList()))
                .historyDtoList(historyList.stream().map(this::entityToHistoryDto).collect(Collectors.toList()))
                .build();
    }

    public OrderShippingDto entityToShippingDto(OrderShipping shipping) {
        return OrderShippingDto.builder()
                .id(shipping.getId())
                .clientAddress(shipping.getClientAddress())
                .clientTelephone(shipping.getClientTelephone())
                .clientName(shipping.getClientName())
                .shippingId(shipping.getShippingId())
                .method(shipping.getMethod())
                .status(shipping.getStatus())
                .trackingCode(shipping.getTrackingCode())
                .carrier(shipping.getCarrier())
                .shippingDate(shipping.getShippingDate())
                .deliveryDate(shipping.getDeliveryDate())
                .shippingFee(shipping.getShippingFee())
                .build();
    }

    public OrderHistoryDto entityToHistoryDto(OrdersHistory history){
        return  OrderHistoryDto.builder()
                .id(history.getId())
                .event(history.getEvent())
                .description(history.getDescription())
                .build();
    }

    public OrderItemDto entityToItemDto(OrdersItems items){
        return OrderItemDto.builder()
                .id(items.getId())
                .productId(items.getProductId())
                .variantId(items.getVariantId())
                .productTitle(items.getProductTitle())
                .productImage(items.getProductImage())
                .variantName(items.getVariantName())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .build();
    }

    public OrderPaymentDto entityToPaymentDto(OrderPayment payment){
        return OrderPaymentDto.builder()
                .id(payment.getId())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionCode(payment.getTransactionCode())
                .transactionNote(payment.getTransactionNote()!=null ? payment.getTransactionNote() : null)
                .amount(payment.getAmount())
                .startDate(payment.getStartDate()!=null ? payment.getStartDate() : null)
                .expiryDate(payment.getExpiryDate()!=null ? payment.getExpiryDate() : null)
                .build();
    }

}

package com.example.ecommerce.mapper.order;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.models.order.*;
import com.example.ecommerce.models.order.check_out.OrderDetailDto;
import com.example.ecommerce.rabbit.elastic.OrderDocument;
import com.example.ecommerce.rabbit.elastic.OrderItemDocument;
import com.example.ecommerce.rabbit.elastic.OrderShopDocument;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import com.example.ecommerce.utils.StringFormatUtils;
import jakarta.persistence.criteria.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final DateTimeMapperUtils timeUtils;
    private final StringFormatUtils stringFormatUtils;

    public OrderShopDocument mapToOrderDocument(OrdersShop order) {
        OrderShopDocument orderDocument = new OrderShopDocument();
        orderDocument.setId(order.getId());
        orderDocument.setOrderShopCode(order.getOrderShopCode());
        orderDocument.setOrderCode(order.getOrdersOfOrdersShop().getOrderCode());
        orderDocument.setStatus(order.getOrderStatus().name());
        orderDocument.setShopShippingPrice(order.getShopShippingFee());
        orderDocument.setShopDiscount(order.getShopDiscount());
        orderDocument.setServiceFee(order.getServiceFee());
        orderDocument.setShopShippingPrice(order.getShopShippingFee());
        orderDocument.setShopDiscount(order.getShopDiscount());
        orderDocument.setTotalAmount(order.getShopTotalAmount());
        orderDocument.setDeliveryMethod(order.getDeliveryMethod());
        orderDocument.setPayment(order.getIsPayment());
        orderDocument.setShopId(order.getShopOfOrdersShop().getId());
        orderDocument.setUserId(order.getOrdersOfOrdersShop().getUsersOfOrder().getId());
        orderDocument.setUpdatedAt(Date.from(order.getModifiedAt().atZone(ZoneId.systemDefault()).toInstant()));
        orderDocument.setItemIds(stringFormatUtils.convertStringToList(order.getItemIds()));
        orderDocument.setPaymentMethod(order.getPaymentMethod());

        return orderDocument;
    }
    public OrderItemDocument mapToOrderItemDocument(OrdersItems ordersItems){
        OrderItemDocument orderItemDocument = new OrderItemDocument();
        orderItemDocument.setId(ordersItems.getId());
        orderItemDocument.setProductId(ordersItems.getProductId());
        orderItemDocument.setVariantId(ordersItems.getVariantId());
        orderItemDocument.setTitle(ordersItems.getProductTitle());
        orderItemDocument.setPrice(ordersItems.getPrice());
        orderItemDocument.setOrderId(ordersItems.getId());
        orderItemDocument.setUrl(ordersItems.getProductImage());
        orderItemDocument.setOrderDate(Date.from(ordersItems.getModifiedAt().atZone(ZoneId.systemDefault()).toInstant()));
        orderItemDocument.setQuantity(ordersItems.getQuantity());
        orderItemDocument.setShopId(ordersItems.getOrdersShopOfOrdersItems().getShopOfOrdersShop().getId());
        return orderItemDocument;
    }
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
                .createdAt(timeUtils.localDateTimeToString(order.getCreatedAt()))
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
                .createdAt(timeUtils.localDateTimeToString(order.getCreatedAt()))
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
                .createdAt(timeUtils.localDateTimeToString(order.getCreatedAt()))
                .build();
    }

    public OrderShopDetail entityToOrderShopDetailDto(OrdersShop order) {
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
                .createdAt(timeUtils.localDateTimeToString(order.getCreatedAt()))
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

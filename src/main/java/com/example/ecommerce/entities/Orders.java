package com.example.ecommerce.entities;

import jakarta.persistence.*;

//@Entity
//@Table(name = "orders")
public class Orders extends BaseEntity{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderCode;
    private String orderStatus;
    private String totalAmount;

    private String deliveryAt;
    private String deliveryMethod;
    private String clientAddress;
    private String clientTelephone;
    private String clientEmail;
    private String clientName;

    private String paymentMethod;
    private String isPayment;
    private Boolean active;
}

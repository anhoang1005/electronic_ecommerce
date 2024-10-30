package com.example.ecommerce.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
//@Entity
//@Table(name = "notification")
public class Notification extends BaseEntity{
    private Long id;
    private String senderCode;
    private String senderName;
    private String title;
    private String image;
    private String detail;
    private Type type;

    public enum Type{
        SYSTEM_NOTIFY,
        ADMIN_NOTIFY,
        SHOP_NOTIFY,
        CUSTOMER_NOTIFY,
        GUEST_NOTIFY
    }

}

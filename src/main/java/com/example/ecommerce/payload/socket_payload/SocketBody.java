package com.example.ecommerce.payload.socket_payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketBody {
    private Object body;
    private String senderCode;
    private String createdAt;
    private Type type;

    public enum Type{
        CHAT,
        UPDATE,
        SENT,
        SEEN,
        SYSTEM_NOTIFY,
        ROOT_NOTIFY,
        SHOP_NOTIFY,
        ADMIN_NOTIFY,
        CUSTOMER_NOTIFY,
        GUEST_NOTIFY
    }
}

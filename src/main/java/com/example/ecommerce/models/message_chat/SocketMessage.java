package com.example.ecommerce.models.message_chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketMessage {
    private Long chatId;
    private String userCode;
    private String shopCode;
    private Long replyTo;
    private String content;
    private String imageUrl;
    private String createdAt;
    private Type type;
    public enum Type{
        CHAT,
        SEEN,
        UPDATE,
        SENT;
    }
}

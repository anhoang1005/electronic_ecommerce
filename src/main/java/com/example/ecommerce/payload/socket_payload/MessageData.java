package com.example.ecommerce.payload.socket_payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageData {
    private Long chatId;
    private String userCode;
    private String shopCode;
    private Long replyTo;
    private String content;
    private String imageUrl;
}

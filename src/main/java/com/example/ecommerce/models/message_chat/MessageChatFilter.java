package com.example.ecommerce.models.message_chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageChatFilter {
    private String userCode;
    private String shopCode;
    private Integer pageNumber;
    private Integer pageSize;
}

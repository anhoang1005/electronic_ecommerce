package com.example.ecommerce.models.message_chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    @JsonProperty("is_shop_sender")
    private Boolean isShopSender;
    @JsonProperty("reply_to")
    private Long replyTo;
    @JsonProperty("reply_message")
    private String replyMessage;
    @JsonProperty("content")
    private String content;
    @JsonProperty("image_Url")
    private String imageUrl;
    @JsonProperty("is_read")
    private Boolean isRead;
    @JsonProperty("created_at")
    private String createdAt;
}

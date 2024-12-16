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
public class MessageChatDto {
    @JsonProperty("chat_id")
    private Long chatId;
    @JsonProperty("online")
    private boolean online;
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("shop_code")
    private String shopCode;
    @JsonProperty("story_avatar")
    private String storyAvatar;
    @JsonProperty("story_name")
    private String storyName;
    @JsonProperty("last_message")
    private String lastMessage;
    @JsonProperty("send_last_at")
    private String sendLastAt;
    @JsonProperty("unread_message_count")
    private Integer unreadMessageCount;
    @JsonProperty("active")
    private Boolean active;
}

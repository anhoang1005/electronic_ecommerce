package com.example.ecommerce.models.message_chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageStoryDto {
    @JsonProperty("chat_id")
    private Long chatId;
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("shop_code")
    private String shopCode;
    @JsonProperty("story_avatar")
    private String storyAvatar;
    @JsonProperty("story_name")
    private String storyName;
    @JsonProperty("unread_message_count")
    private Integer unreadMessageCount;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("list_message")
    private List<MessageDto> listMessage;
}

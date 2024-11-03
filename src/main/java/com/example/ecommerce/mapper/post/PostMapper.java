package com.example.ecommerce.mapper.post;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.models.post.MediaUrl;
import com.example.ecommerce.models.post.PostDto;
import com.example.ecommerce.models.post.SharedPostDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PostDto entityToPostDto(Post post){
        PostDto dto = new PostDto();
        dto.setPostRole(post.getPostRole().toString());
        if(post.getPostRole()== BaseRole.CHUCUAHANG){
            Shop shop = post.getShopOfPost();
            dto.setPostAvatar(shop.getShopLogo());
            dto.setPostName(shop.getShopName());
        } else if(post.getPostRole()== BaseRole.QUANLY){
            Users users = post.getUsersOfPost();
            dto.setPostAvatar(users.getImageUrl());
            dto.setPostName("Admin: " + users.getUsername());
        } else if(post.getPostRole()== BaseRole.ROOT){
            dto.setPostAvatar("https://media.istockphoto.com/id/1416937202/vector/settings-or-gear-icon-cog-setting-vector-illustration.jpg?s=612x612&w=0&k=20&c=3vVNW4ssuNPwKUMT5HSMEbUlknZmp5FeEBF1eZTzJYA=");
            dto.setPostName("System");
        } else{
            Users users = post.getUsersOfPost();
            dto.setPostAvatar(users.getImageUrl());
            dto.setPostName(users.getUsername());
        }
        dto.setId(post.getId());

        dto.setContent(post.getContent()!=null ? post.getContent() : null);
        dto.setMediaUrl(post.getMediaUrl()!=null ? convertToEntityAttribute(post.getMediaUrl()) : null);
        dto.setHashTag(post.getHashtag() != null ? Arrays.asList(post.getHashtag().split(",")) : null);

        dto.setLocation(post.getLocation()!=null ? post.getLocation() : null);
        dto.setActive(post.getActive());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setSharedCount(post.getSharedCount());
        if(post.getIsShared()){
            dto.setSharedPost(entityToSharePostDto(post.getSharedPost()));
        } else{
            dto.setSharedPost(null);
        }
        return dto;
    }

    public SharedPostDto entityToSharePostDto(Post post){
        SharedPostDto dto = new SharedPostDto();
        dto.setPostRole(post.getPostRole().toString());
        dto.setContent(post.getContent()!=null ? post.getContent() : null);
        dto.setMediaUrl(post.getMediaUrl()!=null ? convertToEntityAttribute(post.getMediaUrl()): null);
        dto.setHashTag(post.getHashtag() != null ? Arrays.asList(post.getHashtag().split(",")) : null);
        dto.setSharedPostId(post.getId());
        if(post.getPostRole()==BaseRole.CHUCUAHANG){
            Shop shop = post.getShopOfPost();
            dto.setSharedPostAvatar(shop.getShopLogo());
            dto.setSharedPostName(shop.getShopName());
        } else if(post.getPostRole()== BaseRole.QUANLY){
            Users users = post.getUsersOfPost();
            dto.setSharedPostAvatar(users.getImageUrl());
            dto.setSharedPostName("Admin: " + users.getUsername());
        } else if(post.getPostRole()== BaseRole.ROOT){
            dto.setSharedPostAvatar("https://media.istockphoto.com/id/1416937202/vector/settings-or-gear-icon-cog-setting-vector-illustration.jpg?s=612x612&w=0&k=20&c=3vVNW4ssuNPwKUMT5HSMEbUlknZmp5FeEBF1eZTzJYA=");
            dto.setSharedPostName("System");
        } else{
            Users users = post.getUsersOfPost();
            dto.setSharedPostAvatar(users.getImageUrl());
            dto.setSharedPostName(users.getUsername());
        }
        return dto;
    }

    public String convertToDatabaseColumn(List<MediaUrl> mediaUrls) {
        try {
            return objectMapper.writeValueAsString(mediaUrls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public List<MediaUrl> convertToEntityAttribute(String mediaUrlsJson) {
        try {
            return objectMapper.readValue(mediaUrlsJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, MediaUrl.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}

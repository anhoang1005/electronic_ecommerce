package com.example.ecommerce.mapper.post;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.post.PostDto;
import com.example.ecommerce.models.post.SharedPostDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeMapperUtils timeMapperUtils;

    public PostDto entityToPostDto(Post post){
        PostDto dto = new PostDto();
        dto.setPostRole(post.getPostRole().toString());
        if(post.getPostRole()== BaseRole.CHUCUAHANG){
            Shop shop = post.getShopOfPost();
            dto.setPostAvatar(shop.getShopLogo());
            dto.setPostName(shop.getShopName());
            dto.setShopCode(shop.getShopCode());
        } else if(post.getPostRole()== BaseRole.QUANLY){
            Users users = post.getUsersOfPost();
            dto.setPostAvatar(users.getImageUrl());
            dto.setPostName("Admin: " + users.getUsername());
            dto.setUserCode(users.getUserCode());
        } else if(post.getPostRole()== BaseRole.ROOT){
            dto.setPostAvatar("https://media.istockphoto.com/id/1416937202/vector/settings-or-gear-icon-cog-setting-vector-illustration.jpg?s=612x612&w=0&k=20&c=3vVNW4ssuNPwKUMT5HSMEbUlknZmp5FeEBF1eZTzJYA=");
            dto.setPostName("System");
        } else{
            Users users = post.getUsersOfPost();
            dto.setPostAvatar(users.getImageUrl());
            dto.setPostName(users.getUsername());
            dto.setUserCode(users.getUserCode());
        }
        dto.setId(post.getId());


        dto.setContent(post.getContent()!=null ? post.getContent() : null);
        dto.setMediaUrl(post.getMediaUrl()!=null ?
                convertFromJson(post.getMediaUrl()) : null);
        dto.setHashTag(post.getHashtag() != null ? convertFromJson(post.getHashtag()): null);

        dto.setLocation(post.getLocation()!=null ? post.getLocation() : null);
        dto.setActive(post.getActive());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setSharedCount(post.getSharedCount());
        dto.setIsShared(post.getIsShared());
        dto.setVisibility(post.getVisibility());
        dto.setCreatedAt(timeMapperUtils.localDateTimeToString(post.getCreatedAt()));
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
        dto.setMediaUrl(post.getMediaUrl()!=null ?
                convertFromJson(post.getMediaUrl()): null);
        dto.setHashTag(post.getHashtag() != null ? convertFromJson(post.getHashtag()) : null);
        dto.setSharedPostId(post.getId());
        dto.setCreatedAt(timeMapperUtils.localDateTimeToString(post.getCreatedAt()));
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

    public String convertToJson(List<String> strings) {
        try {
            return objectMapper.writeValueAsString(strings);
        } catch (JsonProcessingException e) {
            throw new RequestNotFoundException("Error convert");
        }
    }

    public List<String> convertFromJson(String json){
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RequestNotFoundException("Error convert");
        }
    }
}

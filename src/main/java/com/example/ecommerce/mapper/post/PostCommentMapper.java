package com.example.ecommerce.mapper.post;

import com.example.ecommerce.entities.PostComment;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.models.post.post_comment.PostCommentDto;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostCommentMapper {
    private final DateTimeMapperUtils timeUtils;


    public PostCommentDto entityToDto(PostComment comment, boolean children){
        PostCommentDto dto = new PostCommentDto();
        Users users = comment.getUsersOfPostComment();
        if(comment.getCommentRole()== BaseRole.CHUCUAHANG){
            Shop shop = comment.getShopOfPostComment();
            dto.setUserAvatar(shop.getShopLogo());
            dto.setUsername(shop.getShopName());
            dto.setShopCode(shop.getShopCode());
        } else if(comment.getCommentRole()== BaseRole.QUANLY || comment.getCommentRole()== BaseRole.ROOT){
            dto.setUserCode(users.getUserCode());
            dto.setUserAvatar(users.getImageUrl());
            dto.setUsername("ELECTROHUB ADMIN");
        } else {
            dto.setUserCode(users.getUserCode());
            dto.setUserAvatar(users.getImageUrl());
            dto.setUsername(users.getUsername());
        }
        dto.setCommentId(comment.getId());
        dto.setLever(comment.getLever());
        dto.setRole(comment.getCommentRole());
        dto.setMediaUrl(comment.getMediaUrl());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(timeUtils.formatFacebookTime(comment.getCreatedAt()));
        if(children){
            dto.setChildren(getChildrenOfComment(comment));
        } else{
            dto.setChildren(null);
        }
        return dto;
    }

    public List<PostCommentDto> getChildrenOfComment(PostComment comment){
        List<PostComment> children = comment.getListPostCommentChildren();
        if(children!=null && !children.isEmpty()){
            return children.stream()
                    .map(cm -> entityToDto(comment, false)).collect(Collectors.toList());
        } else{
            return null;
        }
    }
}

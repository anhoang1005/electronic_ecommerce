package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.PostHashTag;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.post.PostMapper;
import com.example.ecommerce.models.post.MediaUrl;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.PostHashTagRepository;
import com.example.ecommerce.repository.PostRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.PostService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IPostService implements PostService {
    private final AuthenticationUtils authUtils;
    private final DateTimeMapperUtils dateUtils;
    private final UsersRepository usersRepository;
    private final ShopRepository shopRepository;
    private final PostRepository postRepository;
    private final PostHashTagRepository hashTagRepository;
    private final PostMapper postMapper;
    private final FileService fileService;

    @Override
    @Transactional
    public ResponseBody userCreatePost(PostRequest req, List<MultipartFile> listFile) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Post post = new Post();
            if(req.getPostRole()==BaseRole.CHUCUAHANG){
                Shop shop = shopRepository.findShopByShopCode(
                        authUtils.getUserFromAuthentication().getShopCode());
                post.setShopOfPost(shop);
            } else {
                Users users = usersRepository.findUsersByUserCode(userCode);
                post.setUsersOfPost(users);
            }
            post.setIsShared(false);
            post.setPostRole(req.getPostRole());
            post.setContent(req.getContent());
            StringBuilder tagString = new StringBuilder();
            List<PostHashTag> listHashTag = new ArrayList<>();
            for (String hashtag : req.getHashTag()){
                PostHashTag postHashTag = hashTagRepository.findByHashTagName(hashtag);
                if(postHashTag!=null){
                    listHashTag.add(postHashTag);
                } else{
                    PostHashTag newHashTag = new PostHashTag();
                    newHashTag.setHashTagName(hashtag);
                    newHashTag.setActive(true);
                    newHashTag.setPostCount(0L);
                    newHashTag = hashTagRepository.save(newHashTag);
                    listHashTag.add(newHashTag);
                }
                tagString.append(hashtag).append(",");
            }
            post.setHashtag(tagString.toString());
            post.setListHashTagOfPost(listHashTag);
            List<MediaUrl> listMedia = new ArrayList<>();

            if(req.getIsPrivate()){ post.setVisibility(Post.Visibility.PRIVATE); }
            else{ post.setVisibility(Post.Visibility.PUBLIC); }
            post.setLocation(req.getLocation());
            post.setActive(true);
            post = postRepository.save(post);
            if(listFile!=null){
                fileService.uploadListFilePostToCloudinary(post, listFile);
            }
            return new ResponseBody(postMapper.entityToPostDto(post),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Create post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody userUpdatePost(PostRequest req, List<MultipartFile> listFile) {
        return null;
    }

    @Override
    public ResponseBody userDeletePost(Long postId) {
        return null;
    }

    @Override
    public ResponseBody userSharedPost(PostRequest req, Long sharedId) {
        return null;
    }

    @Override
    public ResponseBody userGetTheirListPost() {
        return null;
    }
}
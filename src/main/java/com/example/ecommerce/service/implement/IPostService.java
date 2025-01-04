package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.*;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.mapper.post.PostMapper;
import com.example.ecommerce.models.post.PostDto;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.payload.PageData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.GeminiService;
import com.example.ecommerce.service.PostService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IPostService implements PostService {
    private final AuthenticationUtils authUtils;
    private final UsersRepository usersRepository;
    private final ShopRepository shopRepository;
    private final PostRepository postRepository;
    private final PostHashTagRepository hashTagRepository;
    private final PostMapper postMapper;
    private final FileService fileService;
    private final ObjectMapper objectMapper;
    private final PostReactionRepository postReactionRepository;
    private final GeminiService geminiService;

    @Override
    public ResponseBody guestGetPostHome(String hashtag, String myUserCode, int pageNumber, int pageSize) {
        try{
            PostHashTag postHashTag;
            if(hashtag!=null){
                postHashTag = hashTagRepository.findByHashTagName(hashtag);
            } else{
                postHashTag = null;
            }
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize,
                    Sort.by(Sort.Order.desc("createdAt")));
            Page<Post> page = postRepository.userGetPostHome(postHashTag, pageable);
            List<PostDto> listPost = page.stream()
                    .map((post) -> postMapper.entityToPostDto(post, myUserCode))
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .data(listPost)
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get get post home error! " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public PostHashTag userCreateHashTag(String hashtag) {
        try{
            PostHashTag postHashTag = new PostHashTag();
            postHashTag.setHashTagName(hashtag);
            postHashTag = hashTagRepository.save(postHashTag);
            return postHashTag;
        } catch (Exception e){
            log.error("Create post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userCreatePost(PostRequest req, List<MultipartFile> listFile) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            String check = geminiService.aiCheckPostCreate(req).getData().toString();
            if(check.trim().equals("true")){
                return new ResponseBody(null, ResponseBody.Status.SUCCESS,
                        "Bài viết của bạn vi phạm tiêu chuẩn cộng đồng!",
                        ResponseBody.Code.FORBIDDEN);
            }
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
            post.setVisibility(req.getVisibility());
            post.setLocation(req.getLocation());
            post.setActive(true);

            List<PostHashTag> existingHashTags = hashTagRepository.findByHashTagNameIn(req.getHashTag());
            Set<String> existingHashTagNames = existingHashTags.stream()
                    .map(PostHashTag::getHashTagName)
                    .collect(Collectors.toSet());
            List<PostHashTag> finalHashTagList = new ArrayList<>(existingHashTags);
            for (String hashtag : req.getHashTag()) {
                if (!existingHashTagNames.contains(hashtag)) {
                    PostHashTag newHashTag = new PostHashTag();
                    newHashTag.setHashTagName(hashtag);
                    newHashTag.setListPostOfHashTag(null);
                    newHashTag = hashTagRepository.save(newHashTag);
                    finalHashTagList.add(newHashTag);
                }
            }
            List<String> finalHashTagString = finalHashTagList.stream()
                    .map(PostHashTag::getHashTagName)
                    .collect(Collectors.toList());
            post.setHashtag(postMapper.convertToJson(finalHashTagString));
            post.setListHashTagOfPost(finalHashTagList);
            post = postRepository.save(post);
            List<String> listMedia = new ArrayList<>();
            if(listFile!=null && !listFile.isEmpty()){
                listFile.forEach(multipartFile -> {
                    ResponseBody upload = fileService.uploadToCloudinary(multipartFile);
                    listMedia.add(upload.getData().toString());
                });
                post.setMediaUrl(postMapper.convertToJson(listMedia));
            }
            return new ResponseBody(postMapper.entityToPostDto(post, userCode),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Create post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userUpdatePost(PostRequest req, List<MultipartFile> listFile) {
        try {
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Post post = postRepository.findById(req.getId())
                    .orElseThrow(() -> new RequestNotFoundException("Post not found with ID: " + req.getId()));
            if(!post.getUsersOfPost().getUserCode().equals(userCode)){
                throw new UnauthorizedException("ERROR");
            }
            post.setContent(req.getContent());
            post.setVisibility(req.getVisibility());
            post.setLocation(req.getLocation());
            List<String> oldHashtagList = objectMapper.readValue(post.getHashtag(),
                    new TypeReference<>() {});
            List<String> newHashtagList = req.getHashTag();
            Set<String> hashTagSet = new HashSet<>(oldHashtagList);
            hashTagSet.addAll(newHashtagList);
            List<PostHashTag> existingHashTags = hashTagRepository.findByHashTagNameIn(hashTagSet.stream().toList());
            List<String> existingHashTagsString = existingHashTags.stream()
                    .map(PostHashTag::getHashTagName).collect(Collectors.toList());
            List<PostHashTag> updatedHashTagList = new ArrayList<>(existingHashTags);
            for (String newHashtag : newHashtagList) {
                if (!existingHashTagsString.contains(newHashtag)) {
                    PostHashTag postHashTag = new PostHashTag();
                    postHashTag.setHashTagName(newHashtag);
                    postHashTag = hashTagRepository.save(postHashTag);
                    updatedHashTagList.add(postHashTag);
                }
            }
            List<PostHashTag> hashTagsToRemove = updatedHashTagList.stream()
                    .filter(existingTag -> !newHashtagList.contains(existingTag.getHashTagName()))
                    .collect(Collectors.toList());
            for (PostHashTag hashTag : hashTagsToRemove) {
                updatedHashTagList.remove(hashTag);
            }
            post.setHashtag(postMapper.convertToJson(newHashtagList));
            post.setListHashTagOfPost(updatedHashTagList);
            List<String> listMedia = new ArrayList<>();
            if(listFile!=null && !listFile.isEmpty()){
                listFile.forEach(multipartFile -> {
                    ResponseBody upload = fileService.uploadToCloudinary(multipartFile);
                    listMedia.add(upload.getData().toString());
                });
                post.setMediaUrl(postMapper.convertToJson(listMedia));
            }
            post = postRepository.save(post);
            return new ResponseBody(postMapper.entityToPostDto(post, userCode),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Update post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userDeletePost(Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RequestNotFoundException("Post not found with ID: " + postId));
            if(!post.getUsersOfPost().getUserCode().equals(authUtils.getUserFromAuthentication().getUserCode())){
                throw new UnauthorizedException("ERROR");
            }
            postRepository.delete(post);
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Delete post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userSharedPost(PostRequest req) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Post originalPost = postRepository.findById(req.getSharedId())
                    .orElseThrow(() -> new RequestNotFoundException("Post not found with ID: " + req.getSharedId()));
            Post sharedPost = new Post();
            sharedPost.setContent(req.getContent());
            sharedPost.setIsShared(true);
            sharedPost.setSharedPost(originalPost);
            sharedPost.setPostRole(req.getPostRole());
            if (req.getPostRole() == BaseRole.CHUCUAHANG) {
                Shop shop = shopRepository.findShopByShopCode(
                        authUtils.getUserFromAuthentication().getShopCode());
                sharedPost.setShopOfPost(shop);
            } else {
                Users user = usersRepository.findUsersByUserCode(userCode);
                sharedPost.setUsersOfPost(user);
            }
            sharedPost.setVisibility(req.getVisibility());
            sharedPost.setLocation(req.getLocation());
            sharedPost.setActive(true);
            sharedPost = postRepository.save(sharedPost);
            return new ResponseBody(postMapper.entityToPostDto(sharedPost, userCode),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Share post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody userGetTheirListPost(String postHashtag, int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
            PostHashTag hashTag = hashTagRepository.findByHashTagName(postHashtag);
            Page<Post> page = postRepository.userGetTheirPostOneHashTag(userId, hashTag, pageable);
            List<PostDto> listPost = page.stream()
                    .map(post -> postMapper.entityToPostDto(post, authUtils.getUserFromAuthentication().getUserCode()))
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .data(listPost)
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody guestGetUserPostOther(String userCodeOther, String hashTag, int pageNumber, int pageSize) {
        try{
            String myUserCode = authUtils.getUserFromAuthentication().getUserCode();
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("createdAt")));
            PostHashTag postHashTag = hashTagRepository.findByHashTagName(hashTag);
            Page<Post> page = postRepository.userGetUserPostOther(userCodeOther, postHashTag, Post.Visibility.PUBLIC, pageable);
            List<PostDto> listPost = page.stream()
                    .map(post -> postMapper.entityToPostDto(post, myUserCode))
                    .collect(Collectors.toList());
            PageData pageData = PageData.builder()
                    .data(listPost)
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalAmount(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("Get post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody adminActivePost(Long postId, Boolean status) {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            post.setActive(status);
            postRepository.save(post);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("admin active post error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody useReactionPost(Long postId, PostReaction.Reaction reaction) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode);
            Post post = postRepository.findById(postId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            PostReaction postReaction = postReactionRepository.findPostReactionByPostIdAndUserCode(userCode, postId);
            if(postReaction!=null){
                if(reaction==PostReaction.Reaction.NONE){
                    postReactionRepository.delete(postReaction);
                    postRepository.updatePostLikeCount(postId, false);
                } else {
                    postReaction.setReaction(reaction);
                    postReactionRepository.save(postReaction);
                }
            } else{
                postReaction = new PostReaction();
                postReaction.setPostOfReaction(post);
                postReaction.setUsersOfPostReaction(users);
                postReaction.setReaction(reaction);
                postReactionRepository.save(postReaction);
                postRepository.updatePostLikeCount(postId, true);
            }
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("reaction post error! Error: {}", e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }


}
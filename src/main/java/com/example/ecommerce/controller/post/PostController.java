package com.example.ecommerce.controller.post;

import com.example.ecommerce.entities.PostReaction;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    @PostMapping("/api/guest/hashtag")
    public ResponseEntity<?> userCreateHashtag(
            @RequestParam("hashTag") String hashTag
    ){
        return ResponseEntity.ok(postService.userCreateHashTag(hashTag));
    }

    @PostMapping("/api/all/post/create")
    public ResponseEntity<?> userCreatePostApi(@RequestParam("post_json") String json,
                                               @RequestParam(name = "file", required = false) List<MultipartFile> listFile){
        try {
            PostRequest req = objectMapper.readValue(json, PostRequest.class);
            return ResponseEntity.ok(postService.userCreatePost(req, listFile));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @PutMapping("/api/all/post/update")
    public ResponseEntity<?> userUpdatePostApi(@RequestParam("post_json") String json,
                                               @RequestParam(name = "file", required = false) List<MultipartFile> listFile){
        try {
            PostRequest req = objectMapper.readValue(json, PostRequest.class);
            return ResponseEntity.ok(postService.userUpdatePost(req, listFile));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @DeleteMapping("/api/all/post/delete/{post_id}")
    public ResponseEntity<?> userCreatePostApi(@PathVariable("post_id") Long id){
        return ResponseEntity.ok(postService.userDeletePost(id));
    }

    @PostMapping("/api/all/post/shared")
    public ResponseEntity<?> userSharedPostApi(@RequestBody PostRequest req ){
        return ResponseEntity.ok(postService.userSharedPost(req));
    }

    @GetMapping("/api/all/post/my-post")
    public ResponseEntity<?> userSharedPostApi(@RequestParam(name = "hash_tag", required = false) String postHashTag,
                                               @RequestParam("page_number") int pageNumber,
                                               @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(postService.userGetTheirListPost(postHashTag, pageNumber, pageSize));
    }

    @GetMapping("/api/all/post/user-post")
    public ResponseEntity<?> userSharedPostApi(@RequestParam("user_code") String userCode,
                                               @RequestParam(name = "hash_tag", required = false) String hashTag,
                                               @RequestParam("page_number") int pageNumber,
                                               @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(postService.userGetUserPostOther(userCode, hashTag, pageNumber, pageSize));
    }

    @PutMapping("/api/admin/post/active")
    public ResponseEntity<?> adminActivePost(@RequestParam("post_id") Long postId,
                                             @RequestParam("status") Boolean status){
        return ResponseEntity.ok(postService.adminActivePost(postId, status));
    }

    @PostMapping("/api/all/post/reaction")
    public ResponseEntity<?> userReactionPost(@RequestParam("post_id") Long postId,
                                              @RequestParam("reaction")PostReaction.Reaction reaction){
        return ResponseEntity.ok(postService.useReactionPost(postId, reaction));
    }
}

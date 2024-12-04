package com.example.ecommerce.controller.post;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.post.post_comment.PostCommentRequest;
import com.example.ecommerce.service.PostCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class PostCommentController {
    private final PostCommentService pcmService;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/guest/post/get-comment")
    public ResponseEntity<?> getListCommentOfPost(@RequestParam(name = "post_id", required = false) Long postId,
                                                  @RequestParam(name = "comment_id", required = false) Long commentId,
                                                  @RequestParam("page_number") int pageNumber){
        return ResponseEntity.ok(pcmService.userGetListComment(postId, commentId, pageNumber));
    }

    @PostMapping("/api/all/post/create-comment")
    public ResponseEntity<?> userCreateCommentOfPost(@RequestParam("comment_json") String json,
                                                  @RequestParam("file") MultipartFile file){
        try {
            PostCommentRequest req = objectMapper.readValue(json, PostCommentRequest.class);
            return ResponseEntity.ok(pcmService.userCreateComment(req, file));
        } catch (Exception e){
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @PutMapping("/api/all/post/update-comment")
    public ResponseEntity<?> userUpdateCommentOfPost(@RequestParam("comment_json") String json,
                                                  @RequestParam("file") MultipartFile file){
        try {
            PostCommentRequest req = objectMapper.readValue(json, PostCommentRequest.class);
            return ResponseEntity.ok(pcmService.userUpdateComment(req, file));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @DeleteMapping("/api/all/post/delete-comment/{comment_id}")
    public ResponseEntity<?> getListCommentOfPost(@PathVariable(name = "comment_id") Long commentId){
        return ResponseEntity.ok(pcmService.userDeleteComment(commentId));
    }
}

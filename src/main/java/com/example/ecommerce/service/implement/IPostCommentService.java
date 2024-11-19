package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.PostComment;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.mapper.post.PostCommentMapper;
import com.example.ecommerce.models.post.post_comment.PostCommentDto;
import com.example.ecommerce.models.post.post_comment.PostCommentRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.PostCommentRepository;
import com.example.ecommerce.repository.PostRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.PostCommentService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IPostCommentService implements PostCommentService {
    private final PostCommentRepository pcmRepository;
    private final AuthenticationUtils authUtils;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;
    private final FileService fileService;
    private final ShopRepository shopRepository;
    private final PostCommentMapper commentMapper;


    @Override
    public ResponseBody userGetListComment(Long postId, Long commentId, int pageNumber) {
        try{
            Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Order.asc("createdAt")));
            if(postId!=null && commentId==null){
                Page<PostComment> page = pcmRepository.findPostCommentByPostIdOrCommentId(postId, commentId, 0, pageable);
                List<PostCommentDto> dtoList = page.stream()
                        .map(comment -> commentMapper.entityToDto(comment, false)).collect(Collectors.toList());
                return new ResponseBody(dtoList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                Page<PostComment> page = pcmRepository.findPostCommentByPostIdOrCommentId(postId, commentId, null, pageable);
                List<PostCommentDto> dtoList = page.stream()
                        .map(comment -> commentMapper.entityToDto(comment, false)).collect(Collectors.toList());
                return new ResponseBody(dtoList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.error("get list comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userCreateComment(PostCommentRequest dto, MultipartFile file) {
        try{
            PostComment comment = new PostComment();
            comment.setCommentRole(dto.getRole());
            comment.setContent(dto.getContent());
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            comment.setPostOfPostComment(post);
            if(file!=null && !file.isEmpty()){
                ResponseBody fileResp = fileService.uploadToCloudinary(file);
                comment.setMediaUrl(fileResp.getData().toString());
            }
            if(dto.getRole()== BaseRole.CHUCUAHANG){
                String shopCode = authUtils.getUserFromAuthentication().getShopCode();
                Shop shop = shopRepository.findShopByShopCode(shopCode);
                comment.setShopOfPostComment(shop);
            } else {
                Long userId = authUtils.getUserFromAuthentication().getId();
                Users users = usersRepository.findById(userId)
                        .orElseThrow(()-> new RequestNotFoundException("ERROR"));
                comment.setUsersOfPostComment(users);
            }
            if(dto.getParentId()!=null){
                PostComment parent = pcmRepository.findById(dto.getParentId())
                        .orElseThrow(()-> new RequestNotFoundException("ERROR"));
                comment.setCommentParent(parent);
                comment.setLever(parent.getLever() + 1);
                comment = pcmRepository.save(comment);
                pcmRepository.incrementChildrenCount(parent.getId());
            } else {
                comment.setCommentParent(null);
                comment.setLever(0);
                comment = pcmRepository.save(comment);
            }
            return new ResponseBody(commentMapper.entityToDto(comment, false), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("create comment error! Error: " + e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userUpdateComment(PostCommentRequest dto, MultipartFile file) {
        try{
            PostComment comment = pcmRepository.findById(dto.getCommentId())
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            comment.setContent(dto.getContent());
            if(file!=null && !file.isEmpty()){
                ResponseBody fileResp = fileService.uploadToCloudinary(file);
                comment.setMediaUrl(fileResp.getData().toString());
            }
            comment = pcmRepository.save(comment);
            return new ResponseBody(commentMapper.entityToDto(comment, false), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("update comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userDeleteComment(Long commentId) {
        try{
            String email = authUtils.getUserFromAuthentication().getEmail();
            PostComment comment = pcmRepository.findById(commentId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            if(!comment.getCreatedBy().equals(email)){
                throw  new UnauthorizedException("ERROR");
            } else {
                pcmRepository.delete(comment);
                return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.error("delete comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody userReactionComment(Long commentId) {
        try{
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("reaction comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}

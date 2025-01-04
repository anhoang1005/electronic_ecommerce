package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.PostHashTag;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p from Post p " +
            "where p.active = true " +
            "and (:hashTag is null or :hashTag member of p.listHashTagOfPost)")
    Page<Post> userGetPostHome(@Param("hashTag") PostHashTag hashtag,
                               Pageable pageable);

    @Query(value = "select p from Post p " +
            "where p.active = true " +
            "and p.usersOfPost.id = :userId " +
            "and (:hashTag is null or :hashTag member of p.listHashTagOfPost) ")
    Page<Post> userGetTheirPostOneHashTag(@Param("userId") Long userId,
                                @Param("hashTag") PostHashTag hashTag,
                                Pageable pageable);

    @Query(value = "select p from Post p " +
            "where p.active = true " +
            "and p.visibility = :visibility " +
            "and p.usersOfPost.userCode = :userCode " +
            "and (:hashtag is null or :hashtag member of p.listHashTagOfPost)")
    Page<Post> userGetUserPostOther(@Param("userCode") String userCode,
                                    @Param("hashtag") PostHashTag hashTag,
                                    @Param("visibility") Post.Visibility visibility,
                                    Pageable pageable);

    @Modifying
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    default void updatePostLikeCount(Long postId, boolean isAdd){
        Post post = findById(postId).orElseThrow(() -> new RequestNotFoundException("ERROR"));
        if(isAdd) post.setLikeCount(post.getLikeCount()+1);
        else  post.setLikeCount(post.getLikeCount()-1);
    }

    @Query("SELECT COUNT(r) FROM Post p JOIN p.listReactionOfPost r WHERE p.id = :postId")
    Long countReactionsByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(c) FROM Post p JOIN p.listPostCommentOfPost c WHERE p.id = :postId")
    Long countCommentsByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(sp) FROM Post p JOIN p.listSharedPosts sp WHERE p.id = :postId")
    Long countSharesByPostId(@Param("postId") Long postId);

}

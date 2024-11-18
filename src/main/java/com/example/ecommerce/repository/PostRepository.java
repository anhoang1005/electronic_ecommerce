package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.PostHashTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

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
    @Query(value = "UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    void incrementLikeCount(@Param("id") Long postId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :id")
    void decrementLikeCount(@Param("id") Long postId);
}

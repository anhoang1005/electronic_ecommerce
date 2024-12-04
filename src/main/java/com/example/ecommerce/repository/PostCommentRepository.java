package com.example.ecommerce.repository;

import com.example.ecommerce.entities.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query(value = "select p from PostComment p " +
            "where (:postId is null or p.postOfPostComment.id = :postId) " +
            "and (:commentId is null or p.commentParent.id = :commentId) " +
            "and (:lever is null or p.lever = :lever) ")
    Page<PostComment> findPostCommentByPostIdOrCommentId(@Param("postId") Long postId,
                                              @Param("commentId") Long commentId,
                                              @Param("lever") Integer lever,
                                              Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update PostComment p set p.childrenCount = p.childrenCount + 1 where p.id = :parentId")
    void incrementChildrenCount(@Param("parentId") Long parentId);

    @Modifying
    @Transactional
    @Query(value = "update PostComment p set p.childrenCount = p.childrenCount - 1 where p.id = :parentId")
    void decrementChildrenCount(@Param("parentId") Long parentId);
}

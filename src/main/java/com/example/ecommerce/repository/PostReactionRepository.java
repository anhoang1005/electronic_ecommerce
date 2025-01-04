package com.example.ecommerce.repository;

import com.example.ecommerce.entities.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    @Query(value = "select r from PostReaction r " +
            "where r.usersOfPostReaction.userCode = :userCode " +
            "and r.postOfReaction.id = :postId ")
    PostReaction findPostReactionByPostIdAndUserCode(@Param("userCode") String userCode,
                                                     @Param("postId") Long postId);


}

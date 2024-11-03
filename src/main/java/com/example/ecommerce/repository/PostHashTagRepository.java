package com.example.ecommerce.repository;

import com.example.ecommerce.entities.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    PostHashTag findByHashTagName(String hashtag);
}

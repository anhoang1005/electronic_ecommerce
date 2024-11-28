package com.example.ecommerce.repository;

import com.example.ecommerce.entities.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    PostHashTag findByHashTagName(String hashtag);

    List<PostHashTag> findByHashTagNameIn(List<String> postHashTag);
}

package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_hashtag", indexes = {
        @Index(name = "idx_hashtag_name", columnList = "hash_tag_name", unique = true)
})
public class PostHashTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hash_tag_name", nullable = false, unique = true, length = 50)
    private String hashTagName;

    @Column(nullable = false)
    private Long postCount;

    @Column(nullable = false)
    private Boolean active;

    @ManyToMany(mappedBy = "listHashTagOfPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> listPostOfHashTag = new ArrayList<>();

    @PrePersist
    void onCreate() {
        this.postCount = 0L;
        this.active = true;
    }
}

package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "post", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_shop_id", columnList = "shop_id")
})
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Lob
    @Column(nullable = true, columnDefinition = "TEXT")
    private String mediaUrl;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String location;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String hashtag;

    private Boolean active;

    @Column(nullable = false)
    private Integer sharedCount;
    @Column(nullable = false)
    private Integer likeCount;
    @Column(nullable = false)
    private Integer commentCount;

    @PrePersist
    public void onCreate(){
        this.commentCount = 0;
        this.likeCount = 0;
        this.commentCount = 0;
        this.active = false;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_post_id")
    private Post sharedPost;

    @OneToMany(mappedBy = "sharedPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> listSharedPosts;

    @OneToMany(mappedBy = "postOfPostComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostComment> listPostCommentOfPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private Users usersOfPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = true)
    private Shop shopOfPost;

    public enum Visibility{
        PUBLIC,
        PRIVATE,
    }
}

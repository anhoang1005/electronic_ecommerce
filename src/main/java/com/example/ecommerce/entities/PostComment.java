package com.example.ecommerce.entities;

import com.example.ecommerce.entities.enums.BaseRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "post_comment", indexes = {
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_post_id", columnList = "post_id"),
})
public class PostComment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer lever;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String media_url;

    @Column(nullable = false)
    private Integer like_count;

    @Column(nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BaseRole commentRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PostComment commentParent;

    @OneToMany(mappedBy = "commentParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostComment> listPostCommentChildren;

    @OneToMany(mappedBy = "postCommentOfReaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostReaction> listReactionOfComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postOfPostComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private Users usersOfPostComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = true)
    private Shop shopOfPostComment;

    @PrePersist
    public void onCreate(){
        this.like_count = 0;
        this.active = true;
    }
}

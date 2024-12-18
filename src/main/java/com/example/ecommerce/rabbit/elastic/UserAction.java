package com.example.ecommerce.rabbit.elastic;

import com.example.ecommerce.entities.PostReaction;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAction implements Serializable {
    private Long userId;
    private ProductAction productAction;
    private PostAction postAction;
    private Action action;
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Builder
    public static class ProductAction{
        private Long productId;
        private Integer rating;
        private State state;
    }

    @Getter
    @Setter
    @Builder
    public static class PostAction{
        private Long postId;
        private PostReaction.Reaction reaction;
        private State state;
    }

    public enum Action{
        LIKE_PRODUCT,
        REVIEW_PRODUCT,
        VIEW_PRODUCT,
        ADD_CART_PRODUCT,
        BUY_PRODUCT,
        VIEW_POST,
        COMMENT_POST,
        REACTION_POST,
    }

    public enum State{
        CREATE,
        UPDATE,
        DELETE
    }
}

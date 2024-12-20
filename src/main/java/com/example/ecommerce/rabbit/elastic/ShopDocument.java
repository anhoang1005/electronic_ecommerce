package com.example.ecommerce.rabbit.elastic;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDocument implements Serializable {
    private Long shopId;
    private String shopCode;
    private Integer rating;
    private State state;


    public enum Action{
        FOLLOW_SHOP,
        REVIEW_SHOP,

    }

    public enum State{
        CREATE,
        UPDATE,
        DELETE
    }
}

package com.example.ecommerce.models.post;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFilter {
    private String myUserCode;
    private String userCodeOther;
    private String hashTag;
    private int pageNumber;
    private int pageSize;
}

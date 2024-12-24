package com.example.ecommerce.models.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCard {
    private String userCode;
    private String userName;
    private String userAvatar;
}

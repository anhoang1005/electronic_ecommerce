package com.example.ecommerce.models.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersFilter {
    private Boolean active;
    private String userCode;
    private String email;
    private String telephone;
    private int pageNumber;
    private int pageSize;
}

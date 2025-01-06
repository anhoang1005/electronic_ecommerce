package com.example.ecommerce.rabbit.elastic;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocument implements Serializable {
    private Long userId;
    private String userCode;
    private String fullName;
    private String username;
    private String gender;
    private String email;
    private String phoneNumber;
    private String roles;
    private State state;

    public enum State{
        CREATE,
        UPDATE,
        DELETE
    }
}

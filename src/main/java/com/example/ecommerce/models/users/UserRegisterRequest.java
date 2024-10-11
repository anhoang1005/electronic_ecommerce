package com.example.ecommerce.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String firstName;
    private String lastName;
    private String dob;
    private Integer isMale;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
}

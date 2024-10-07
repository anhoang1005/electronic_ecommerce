package com.example.ecommerce.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtData {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("date_of_birth")
    private String dob;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private List<String> role;

    @JsonProperty("issued_at")
    private LocalDateTime issuedAt;

    @JsonProperty("expire_at")
    private LocalDateTime expiresAt;
}

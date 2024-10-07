package com.example.ecommerce.models.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDetailResponse {
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("date_of_birth")
    private String dob;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("username")
    private String username;
    @JsonProperty("list_role")
    private List<String> role;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("modified_at")
    private String modifiedAt;
    @JsonProperty("modified_by")
    private String modifiedBy;
}

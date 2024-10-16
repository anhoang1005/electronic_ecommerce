package com.example.ecommerce.mapper.users;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.models.users.UsersDetailResponse;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsersMapper {
    private final DateTimeMapperUtils dateMapperUtils;
    public UsersMapper(DateTimeMapperUtils dateMapperUtils) {
        this.dateMapperUtils = dateMapperUtils;
    }

    public UsersDetailResponse entityToUsersDetailResponse(Users entity){
        return UsersDetailResponse.builder()
                .userCode(entity.getUserCode())
                .imageUrl(entity.getImageUrl())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dob(dateMapperUtils.localDateToString(entity.getDob()))
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .role(entity.getListRolesOfUsers().stream().map(Roles::getRoleName).collect(Collectors.toList()))
                .active(entity.getActive())
                .enable(entity.getEnable())
                .createdAt(dateMapperUtils.localDateTimeToString(entity.getCreatedAt()))
                .modifiedAt(dateMapperUtils.localDateTimeToString(entity.getModifiedAt()))
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}

package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.mapper.users.UsersMapper;
import com.example.ecommerce.models.users.UserRegisterRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.FileService;
import com.example.ecommerce.service.UsersService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class IUsersService implements UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUtils authenticationUtils;
    private final DateTimeMapperUtils dateMapperUtils;
    private final FileService fileService;
    private final RolesRepository rolesRepository;

    @Override
    @Transactional
    public ResponseBody usersChangePassword(String email, String oldPassword, String newPassword) {
        if(!authenticationUtils.checkUsersAuthentication(email)){
            throw new UnauthorizedException("Unauthorized!");
        }
        Users usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity!=null && passwordEncoder.matches(oldPassword, usersEntity.getPasswordHash())){
            usersEntity.setPasswordHash(passwordEncoder.encode(newPassword));
            usersRepository.save(usersEntity);
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } else{
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "INCORRECT_PASSWORD", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    @Transactional
    public ResponseBody usersChangeUserInfo(UserRegisterRequest info) {
        if(!authenticationUtils.checkUsersAuthentication(info.getEmail())){
            throw new UnauthorizedException("Unauthorized!");
        }
        Users checkExistedUsername = usersRepository.findUsersEntitiesByUsername(info.getUsername());
        if(checkExistedUsername!=null && !checkExistedUsername.getUsername().equals(info.getUsername())){
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "EXISTED_USERNAME", ResponseBody.Code.SUCCESS);
        }
        Users user = usersRepository.findUsersEntitiesByEmail(info.getEmail());
        user.setUsername(info.getUsername());
        user.setPhoneNumber(info.getPhoneNumber());
        user.setDob(dateMapperUtils.stringToLocalDate(info.getDob()));
        user.setLastName(info.getLastName());
        user.setFirstName(info.getFirstName());
        usersRepository.save(user);
        return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }

    @Override
    @Transactional
    public ResponseBody usersChangeAvatar(String email, MultipartFile file) {
        if(!authenticationUtils.checkUsersAuthentication(email)){
            throw new UnauthorizedException("Unauthorized!");
        }
        Users usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity!=null){
            ResponseBody responseImage = fileService.uploadToCloudinary(file);
            usersEntity.setImageUrl(responseImage.getData().toString());
            usersRepository.save(usersEntity);
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } else{
            throw new RequestNotFoundException("Email not existed");
        }
    }

    @Override
    public ResponseBody usersGetUsersDetailResponse(String email) {
        if(!authenticationUtils.checkUsersAuthentication(email)){
            throw new UnauthorizedException("Unauthorized!");
        }
        Users usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity!=null){
            return new ResponseBody(
                    usersMapper.entityToUsersDetailResponse(usersEntity),
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS
            );
        } else{
            throw new RequestNotFoundException("Bad request");
        }
    }

    @Override
    public ResponseBody rootChangeRoleUsers(String userCode, boolean isAdmin) {
        try {
            Users users = usersRepository.findUsersByUserCode(userCode);
            List<Roles> rolesUsers = users.getListRolesOfUsers();

            //Kiem tra nguoi dung co quyen quan li hay khong
            boolean hasQuanLiRole = rolesUsers.stream().anyMatch(role -> role.getRoleName().equals("QUANLI"));

            if (isAdmin && !hasQuanLiRole) {
                rolesRepository.findByRoleName("QUANLI")
                        .ifPresent(rolesUsers::add);
            } else if (!isAdmin && hasQuanLiRole) {
                rolesUsers.removeIf(role -> role.getRoleName().equals("QUANLI"));
            }
            users.setListRolesOfUsers(rolesUsers);
            usersRepository.save(users);

            log.info("Root users change users {} to admin success!", userCode);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Root users change users to {} admin failed! Error: {}", userCode, e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }



}

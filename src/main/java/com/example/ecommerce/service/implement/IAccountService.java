package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.AccountLockedException;
import com.example.ecommerce.exceptions.users.InvalidCredentialsException;
import com.example.ecommerce.models.users.UserRegisterRequest;
import com.example.ecommerce.payload.JwtData;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.AccountService;
import com.example.ecommerce.service.MailService;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import com.example.ecommerce.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class IAccountService implements AccountService {

    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;
    private final JwtTokenUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final DateTimeMapperUtils dateMapperUtils;
    private final MailService mailService;


    public String generateVerifyCode() {
        String character = "0123456789";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(character.length());
            password.append(character.charAt(randomIndex));
        }
        return password.toString();
    }

    @Override
    public JwtData loginUsers(String username, String password) {
        Users usersEntity = usersRepository.findUsersEntitiesByEmailOrUsername(username, username);
        if (usersEntity != null && passwordEncoder.matches(password, usersEntity.getPasswordHash())) {
            if (usersEntity.getActive()) {
                List<String> listRoleString = usersEntity.getListRolesOfUsers().stream()
                        .map(Roles::getRoleName)
                        .collect(Collectors.toList());
                String accessJws = jwtUtils.generateAccessTokens(username, listRoleString);
                String refreshJws = jwtUtils.generateRefreshTokens(username, listRoleString);
                return JwtData.builder()
                        .tokenType("Bearer ")
                        .accessToken(accessJws)
                        .refreshToken(refreshJws)
                        .dob(dateMapperUtils.localDateToString(usersEntity.getDob()))
                        .fullName(usersEntity.getFirstName() + " " + usersEntity.getLastName())
                        .imageUrl(usersEntity.getImageUrl())
                        .email(usersEntity.getEmail())
                        .phoneNumber(usersEntity.getPhoneNumber())
                        .issuedAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusDays(3))
                        .role(listRoleString)
                        .build();
            } else {
                log.info("Account Locked!");
                throw new AccountLockedException("Account Locked!");
            }
        } else {
            log.info("Invalid Account!");
            throw new InvalidCredentialsException("Invalid Account!");
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public ResponseBody registerUsers(UserRegisterRequest user) {
        try {
            Users checkUsername = usersRepository.findUsersEntitiesByUsername(user.getUsername());
            if (checkUsername != null) {
                return new ResponseBody(null, ResponseBody.Status.SUCCESS, "USERNAME_EXISTED", ResponseBody.Code.SUCCESS);
            }
            Users checkEmail = usersRepository.findUsersEntitiesByEmail(user.getEmail());
            if (checkEmail != null && checkEmail.getEnable()) {
                return new ResponseBody(null, ResponseBody.Status.SUCCESS, "EMAIL_EXISTED", ResponseBody.Code.SUCCESS);
            } else{
                Optional<Roles> roleOptional = rolesRepository.findByRoleName("KHACHHANG");
                List<Roles> roleEntityList = new ArrayList<>();
                roleOptional.ifPresent(roleEntityList::add);
                Users usersEntity = new Users();
                String verifyCode = generateVerifyCode();
                usersEntity.setDob(dateMapperUtils.stringToLocalDate(user.getDob()));
                usersEntity.setFirstName(user.getFirstName());
                usersEntity.setLastName(user.getLastName());
                usersEntity.setGender((user.getIsMale() == 1) ? Users.Gender.NAM : Users.Gender.NU);
                usersEntity.setEmail(user.getEmail());
                usersEntity.setPhoneNumber(user.getPhoneNumber());
                usersEntity.setEnable(false);
                usersEntity.setUsername(user.getUsername());
                usersEntity.setActive(true);
                usersEntity.setListRolesOfUsers(roleEntityList);
                usersEntity.setImageUrl("user.jpg");
                usersEntity.setVerifyCode(passwordEncoder.encode(verifyCode));
                usersEntity.setPasswordHash(passwordEncoder.encode(user.getPassword()));
                usersRepository.save(usersEntity);
                mailService.sendVerifyRegisterEmail(user.getEmail(), verifyCode);
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.info("Register account failed " + e.getMessage());
            throw new RequestNotFoundException("Register Failed!");
        }
    }

    @Transactional
    @Override
    public ResponseBody checkVerifyCodeRegister(String email, String verifyCode) {
        Users user = usersRepository.findUsersEntitiesByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(verifyCode, user.getVerifyCode())) {
                user.setEnable(true);
                user.setVerifyCode(null);
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
            }
        } else {
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "EMAIL_NOT_EXISTED", ResponseBody.Code.SUCCESS);
        }
    }


    @Transactional
    @Override
    public ResponseBody userForgotPassword(String email) {
        try {
            Users usersEntity = usersRepository.findUsersEntitiesByEmail(email);
            if (usersEntity != null) {
                String verifyCode = generateVerifyCode();
                usersEntity.setVerifyCode(passwordEncoder.encode(verifyCode));
                mailService.sendFogotPasswordMail(
                        usersEntity.getFirstName() + " " + usersEntity.getLastName(),
                        email,
                        verifyCode);
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "USER_NOT_EXISTED", ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Forget Password Failed");
            throw new RequestNotFoundException("Error");
        }
    }

    @Transactional
    @Override
    public ResponseBody checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode) {
        Users usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity != null){
            if(passwordEncoder.matches(verifyCode, usersEntity.getVerifyCode())){
                usersEntity.setPasswordHash(passwordEncoder.encode(newPassword));
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else{
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
            }
        } else{
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "EMAIL_NOT_EXISTED", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    public ResponseBody generateRootUsers() {
        Optional<Roles> roleOptional = rolesRepository.findByRoleName("ROOT");
        List<Roles> roleEntityList = new ArrayList<>();
        roleOptional.ifPresent(roleEntityList::add);
        Users usersEntity = new Users();
        String verifyCode = generateVerifyCode();
        usersEntity.setDob(dateMapperUtils.stringToLocalDate("2002-10-10"));
        usersEntity.setFirstName("Hoang");
        usersEntity.setLastName("Van An");
        usersEntity.setGender(Users.Gender.NAM);
        usersEntity.setEmail("anhan10056@gmail.com");
        usersEntity.setPhoneNumber("0987654321");
        usersEntity.setEnable(true);
        usersEntity.setUsername("root2002");
        usersEntity.setActive(true);
        usersEntity.setListRolesOfUsers(roleEntityList);
        usersEntity.setImageUrl("user.jpg");
        usersEntity.setVerifyCode(passwordEncoder.encode(verifyCode));
        usersEntity.setPasswordHash(passwordEncoder.encode("12345678"));
        usersRepository.save(usersEntity);
        return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }
}

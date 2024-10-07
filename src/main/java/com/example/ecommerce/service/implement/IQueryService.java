package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.RoleEntity;
import com.example.ecommerce.entities.UsersEntity;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class IQueryService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final DateTimeMapperUtils dateMapperUtils;

    @Transactional(rollbackFor = {Exception.class})
    public boolean createRootRoles(){
        try{
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRoleCode("RT");
            roleEntity.setRoleName("ROOT");
            roleEntity.setActive(true);
            roleEntity.setNote("Tài khoản gốc của hệ thống");
            rolesRepository.save(roleEntity);

            roleEntity = new RoleEntity();
            roleEntity.setRoleCode("QL");
            roleEntity.setRoleName("QUANLI");
            roleEntity.setActive(true);
            roleEntity.setNote("Tài khoản của quản lí hệ thống");
            rolesRepository.save(roleEntity);

            roleEntity = new RoleEntity();
            roleEntity.setRoleCode("KH");
            roleEntity.setRoleName("KHACHHANG");
            roleEntity.setActive(true);
            roleEntity.setNote("Tài khoản của khách hàng");
            rolesRepository.save(roleEntity);

            roleEntity = new RoleEntity();
            roleEntity.setRoleCode("CV");
            roleEntity.setRoleName("CONGTACVIEN");
            roleEntity.setActive(true);
            roleEntity.setNote("Tài khoản của cộng tác viên chủ cửa hàng");
            rolesRepository.save(roleEntity);
            log.info("Create root Role success");
            return true;
        } catch (Exception e){
            log.warn("Create Role Failed");
            return false;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean createRootAccount(){
        try{
            Optional<RoleEntity> roleEntity = rolesRepository.findByRoleName("ROOT");
            List<RoleEntity> listRoles = new ArrayList<>();
            roleEntity.ifPresent(listRoles::add);
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setFirstName("Hoàng");
            usersEntity.setLastName("Văn An");
            usersEntity.setDob(dateMapperUtils.stringToLocalDate("2002-05-10"));
            usersEntity.setPhoneNumber("0867865001");
            usersEntity.setEmail("anhoang10052002@gmail.com");
            usersEntity.setImageUrl("me.jpg");
            usersEntity.setActive(true);
            usersEntity.setEnable(true);
            usersEntity.setUsername("anhoang2002");
            usersEntity.setPasswordHash("$2a$12$AdVBsX7F.jEbqnYUoLNk1eQTX2AlXzPKek7HTqf/DcMcjMlQ6P12u"); //10052002
            usersEntity.setVerifyCode(null);
            usersEntity.setRoles(listRoles);
            usersRepository.save(usersEntity);
            log.info("Create root account success");
            return true;
        } catch (Exception e){
            log.warn("Create Root Account failed");
            return false;
        }
    }

}

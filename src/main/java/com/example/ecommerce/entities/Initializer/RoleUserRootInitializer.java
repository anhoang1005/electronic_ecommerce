package com.example.ecommerce.entities.Initializer;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleUserRootInitializer implements CommandLineRunner {

    private final DateTimeMapperUtils dateTimeMapperUtils;
    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;
    public RoleUserRootInitializer(RolesRepository rolesRepository, UsersRepository usersRepository, DateTimeMapperUtils dateTimeMapperUtils) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.dateTimeMapperUtils = dateTimeMapperUtils;
    }

    @Override
    public void run(String... args) {
        try {
            if (!rolesRepository.existsByRoleName("ROOT")) {
                Roles roleEntity = new Roles();
                roleEntity.setRoleCode("RT");
                roleEntity.setRoleName("ROOT");
                roleEntity.setActive(true);
                roleEntity.setNote("Tài khoản gốc của hệ thống");
                rolesRepository.save(roleEntity);
            }
            if (!rolesRepository.existsByRoleName("QUANLI")) {
                Roles roleEntity = new Roles();
                roleEntity.setRoleCode("QL");
                roleEntity.setRoleName("QUANLI");
                roleEntity.setActive(true);
                roleEntity.setNote("Tài khoản của quản lí hệ thống");
                rolesRepository.save(roleEntity);
            }
            if (!rolesRepository.existsByRoleName("KHACHHANG")) {
                Roles roleEntity = new Roles();
                roleEntity.setRoleCode("KH");
                roleEntity.setRoleName("KHACHHANG");
                roleEntity.setActive(true);
                roleEntity.setNote("Tài khoản của khách hàng");
                rolesRepository.save(roleEntity);
            }
            if (!rolesRepository.existsByRoleName("CONGTACVIEN")) {
                Roles roleEntity = new Roles();
                roleEntity.setRoleCode("CV");
                roleEntity.setRoleName("CONGTACVIEN");
                roleEntity.setActive(true);
                roleEntity.setNote("Tài khoản của cộng tác viên chủ cửa hàng");
                rolesRepository.save(roleEntity);
            }
            log.info("Create all root roles success!");
        } catch (Exception e) {
            log.error("Create root error! Error: {}", e.getMessage());
        }
    }
}

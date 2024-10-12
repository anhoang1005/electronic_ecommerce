package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findUsersEntitiesByUsername(String username);
    Users findUsersEntitiesByEmail(String email);
    Users findUsersEntitiesByEmailOrUsername(String email, String username);
    Users findUsersByUserCode(String userCode);
}

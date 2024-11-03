package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findUsersById(Long id);
    Users findUsersEntitiesByUsername(String username);
    Users findUsersEntitiesByEmail(String email);
    Users findUsersEntitiesByEmailOrUsername(String email, String username);
    Users findUsersByUserCode(String userCode);
    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
            "LEFT JOIN roles r ON ur.role_id = r.id " +
            "GROUP BY u.id " +
            "HAVING SUM(CASE WHEN r.id = 3 THEN 1 ELSE 0 END) = 0 " +
            "LIMIT 100", nativeQuery = true)
    List<Users> find100UserWithoutRoleId3();
}

package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "SELECT * FROM users ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Users> findRandomUsers(@Param("count") int count);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    default Users findAndUpdateReadNotify(String userCode, Long notifyId){
        Users users = findUsersByUserCode(userCode);
        users.setReadNotifyId(notifyId);
        return save(users);
    }

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

    @Query("select u from Users u " +
            "where (:userCode is null or u.userCode = :userCode) " +
            "and (:email is null or u.email like %:email%) " +
            "and (:phoneNumber is null or u.phoneNumber like %:phoneNumber%)" +
            "and (:active is null or u.active = :active)")
    Page<Users> rootGetDashBoard(@Param("userCode") String userCode,
                                 @Param("email") String email,
                                 @Param("phoneNumber") String phoneNumber,
                                 @Param("active") Boolean active,
                                 Pageable pageable);
}

package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

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
}

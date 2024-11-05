package com.example.ecommerce.repository;

import com.example.ecommerce.entities.NotificationUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationUsersRepository extends JpaRepository<NotificationUsers, Long> {

    @Query(value = "select nu from NotificationUsers nu " +
            "where (nu.usersOfNotifyUsers is null or nu.usersOfNotifyUsers.id = :userId) ")
    Page<NotificationUsers> customerGetTheirNotification(@Param("userId") Long userId,
                                                    Pageable pageable);

    @Query(value = "select nu from NotificationUsers nu " +
            "where nu.notifyOfNotifyUser.id = :notifyId")
    NotificationUsers findByNotifyId(@Param("notifyId") Long notifyId);
}

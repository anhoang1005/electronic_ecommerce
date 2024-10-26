package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Message;
import com.example.ecommerce.entities.MessageChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageChatRepository extends JpaRepository<MessageChat, Long> {

    @Query(value = "select mc from MessageChat mc " +
            "where (:shopCode is null or mc.shopOfMessageStory.shopCode = :shopCode) " +
            "and (:userCode is null or mc.usersOfMessageStory.userCode = :userCode) " +
            "and mc.active = true ")
    Page<MessageChat> getListMessageChatOfShop(@Param("shopCode") String shopCode,
                                               @Param("userCode") String userCode,
                                               Pageable pageable);

    @Query(value = "select mc from MessageChat mc " +
            "where mc.usersOfMessageStory.userCode = :userCode " +
            "and mc.shopOfMessageStory.shopCode = :shopCode ")
    Optional<MessageChat> getMessageChatByShopCodeAndUserCode(@Param("userCode") String userCode,
                                                    @Param("shopCode") String shopCode);
}

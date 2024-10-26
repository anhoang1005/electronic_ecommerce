package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "select m from Message m " +
            "where m.messageStoryOfMessage.id = :chatId")
    Page<Message> getListMessageByChatId(@Param("chatId") Long chatId, Pageable pageable);

    Message findMessageById(Long id);
}

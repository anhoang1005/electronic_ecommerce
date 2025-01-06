package com.example.ecommerce.rabbit;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.rabbit.elastic.UserDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserProducer {
    private final AmqpTemplate amqpTemplate;
    private final String recommendExchange;
    private final String userRecommendRoutingKey;
    private final ObjectMapper objectMapper;

    public UserProducer(AmqpTemplate amqpTemplate,
                        @Value("${rabbitmq.exchange.recommend.name}") String recommendExchange,
                        @Value("${rabbitmq.routing.user.recommend.key}") String userRecommendRoutingKey, ObjectMapper objectMapper){
        this.amqpTemplate = amqpTemplate;
        this.recommendExchange = recommendExchange;
        this.userRecommendRoutingKey = userRecommendRoutingKey;
        this.objectMapper = objectMapper;
    }

    public void sendUserToQueue(Users user){
        try{
            List<String> roleString = user.getListRolesOfUsers().stream().map(Roles::getRoleName).collect(Collectors.toList());
            UserDocument userDocument = UserDocument.builder()
                    .userId(user.getId())
                    .userCode(user.getUserCode())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .gender(user.getGender().name())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .roles(objectMapper.writeValueAsString(roleString))
                    .state(UserDocument.State.CREATE)
                    .build();
            amqpTemplate.convertAndSend(recommendExchange, userRecommendRoutingKey, userDocument);
            log.info("User sent to rabbitmq : " + userDocument.getUserId());
        } catch (Exception e){
            log.info("Failed sent user to rabbitmq!");
        }
    }

    public void sendListUserToQueue(List<Users> userList){
        try{
            for (Users user : userList){
                List<String> roleString = user.getListRolesOfUsers().stream().map(Roles::getRoleName).collect(Collectors.toList());
                UserDocument userDocument = UserDocument.builder()
                        .userId(user.getId())
                        .userCode(user.getUserCode())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .gender(user.getGender().name())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .roles(objectMapper.writeValueAsString(roleString))
                        .build();
                amqpTemplate.convertAndSend(recommendExchange, userRecommendRoutingKey, userDocument);
                log.info("User sent to rabbitmq : " + userDocument.getUserId());
            }
        } catch (Exception e){
            log.info("Failed sent user to rabbitmq!");
        }
    }
}

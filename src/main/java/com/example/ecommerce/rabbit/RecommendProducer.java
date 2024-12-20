package com.example.ecommerce.rabbit;

import com.example.ecommerce.entities.PostReaction;
import com.example.ecommerce.rabbit.elastic.UserAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RecommendProducer {

    private final AmqpTemplate amqpTemplate;
    private final String recommendExchange;
    private final String recommendRoutingKey;

    public RecommendProducer (AmqpTemplate amqpTemplate,
                            @Value("${rabbitmq.exchange.recommend.name}") String recommendExchange,
                            @Value("${rabbitmq.routing.recommend.key}") String recommendRoutingKey){
        this.amqpTemplate = amqpTemplate;
        this.recommendExchange = recommendExchange;
        this.recommendRoutingKey = recommendRoutingKey;
    }

    public void sendShopToRecommendQueue(UserAction userAction){
        log.info("User action sent to rabbitmq : " + userAction.getUserId());
    }

    public void sendProductActionToRecommendQueue(Long userId, Long productId,
                                                  UserAction.Action action,
                                                  UserAction.State state, Integer rating){
        UserAction userAction = UserAction.builder()
                .userId(userId)
                .productAction(UserAction.ProductAction.builder()
                        .productId(productId)
                        .rating(rating)
                        .state(state)
                        .build())
                .action(action)
                .createdAt(LocalDateTime.now())
                .build();
        amqpTemplate.convertAndSend(recommendExchange, recommendRoutingKey , userAction);
        log.info("Product action sent to rabbitmq : " + userAction.getUserId());
    }

    public void sendPostActionToRecommendQueue(Long userId, Long postId,
                                               UserAction.Action action,
                                               UserAction.State state,
                                               PostReaction.Reaction reaction){
        UserAction userAction = UserAction.builder()
                .userId(userId)
                .postAction(UserAction.PostAction.builder()
                        .postId(postId)
                        .reaction(reaction)
                        .state(state)
                        .build())
                .action(action)
                .createdAt(LocalDateTime.now())
                .build();
        amqpTemplate.convertAndSend(recommendExchange, recommendRoutingKey , userAction);
        log.info(" action sent to rabbitmq : " + userAction.getUserId());
    }
}

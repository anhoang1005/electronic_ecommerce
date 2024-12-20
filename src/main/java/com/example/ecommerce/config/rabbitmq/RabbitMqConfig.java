package com.example.ecommerce.config.rabbitmq;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.host}")
    private String RABBIT_HOST;
    @Value("${spring.rabbitmq.port}")
    private int RABBIT_PORT;
    @Value("${spring.rabbitmq.username}")
    private String RABBIT_USER;
    @Value("${spring.rabbitmq.password}")
    private String RABBIT_PASSWORD;

    //Product
    @Value("${rabbitmq.exchange.product.name}")
    private String productExchangeName;
    @Value("${rabbitmq.queue.product}")
    private String productQueueName;
    @Value("${rabbitmq.routing.product.key}")
    private String productRoutingKey;

    //Recommend
    @Value("${rabbitmq.exchange.recommend.name}")
    private String recommendExchangeName;
    @Value("${rabbitmq.queue.recommend}")
    private String recommendQueueName;
    @Value("${rabbitmq.queue.recommend.product}")
    private String recommendProductQueueName;
    @Value("${rabbitmq.routing.product.recommend.key}")
    private String productRecommendRoutingKey;
    @Value("${rabbitmq.routing.recommend.key}")
    private String recommendRoutingKey;

    // Product
    @Bean
    public Queue productQueue() {
        return new Queue(productQueueName, true); // durable = true
    }

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(productExchangeName, true, false);
    }

    @Bean
    public Binding productBinding() {
        return BindingBuilder
                .bind(productQueue())
                .to(productExchange())
                .with(productRoutingKey);
    }

    // Recommend
    @Bean
    public Queue recommendQueue() {
        return new Queue(recommendQueueName, true);
    }

    @Bean
    public Queue recommendProductQueue() {
        return new Queue(recommendProductQueueName, true);
    }

    @Bean
    public DirectExchange recommendExchange() {
        return new DirectExchange(recommendExchangeName, true, false);
    }

    @Bean
    public Binding recommendBinding() {
        return BindingBuilder
                .bind(recommendQueue())
                .to(recommendExchange())
                .with(recommendRoutingKey);
    }

    @Bean
    public Binding productRecommendBinding() {
        return BindingBuilder
                .bind(recommendProductQueue())
                .to(recommendExchange())
                .with(productRecommendRoutingKey);
    }

    // 3. Cấu hình MessageConverter với Jackson (cho JSON)
    @Bean
    public MessageConverter converter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Hỗ trợ Java 8 date/time
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBIT_HOST);
        connectionFactory.setUsername(RABBIT_USER);
        connectionFactory.setPassword(RABBIT_PASSWORD);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(RABBIT_PORT);
        connectionFactory.setChannelCacheSize(25); // Cấu hình kích thước bộ nhớ cache của kênh

        return connectionFactory;
    }

    // 4. Cấu hình RabbitTemplate
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    // 5. Cấu hình Listener Container Factory
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setPrefetchCount(50);
        return factory;
    }
}

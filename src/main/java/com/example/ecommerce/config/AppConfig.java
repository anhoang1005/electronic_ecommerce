package com.example.ecommerce.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }
    @Bean
    public Faker faker(){
        return new Faker();
    }
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public Random random(){
        return new Random();
    }
}

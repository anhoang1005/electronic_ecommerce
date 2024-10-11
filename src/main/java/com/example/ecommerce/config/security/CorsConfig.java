package com.example.ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Áp dụng cho tất cả các đường dẫn
//                        .allowedOrigins("*") // Cho phép goi api tu duong dan khac
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTION") // Các phương thức cho phép
//                        .allowedHeaders("*") // Cho phép tất cả các header
//                        .allowCredentials(true); // Cho phép gửi cookie và xác thực
//            }
//        };
//    }
//}

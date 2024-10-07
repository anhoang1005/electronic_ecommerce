package com.example.ecommerce.config.aspect;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Ddt so luong request toi da trong mot khoang thoi gian, de tranh bi tinh trang tan cong DOS
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
}

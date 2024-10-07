package com.example.ecommerce.config.aspect;

import com.example.ecommerce.exceptions.aspect.TooManyRequestsException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Ddt so luong request toi da trong mot khoang thoi gian, de tranh bi tinh trang tan cong DOS
 */
@Aspect
public class RateLimitingAspect {

    private final RateLimiter rateLimiter = new RateLimiter();

    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void limitRequests() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIp = request.getRemoteAddr();

        if (!rateLimiter.allowRequest(clientIp)) {
            throw new TooManyRequestsException("Too many requests from this IP");
        }
    }
}

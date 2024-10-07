package com.example.ecommerce.config.aspect;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Ddt so luong request toi da trong mot khoang thoi gian, de tranh bi tinh trang tan cong DOS
 */
public class RateLimiter {
    private static final long TIME_WINDOW = 60 * 1000; // Thoi gian trong 1 phut(60 giay)
    private static final int MAX_REQUESTS = 100; // So luong request toi da

    private final ConcurrentHashMap<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    public boolean allowRequest(String ip) {
        long now = System.currentTimeMillis();

        requestCounts.compute(ip, (key, requestInfo) -> {
            if (requestInfo == null || now - requestInfo.timestamp > TIME_WINDOW) {
                // Reset count neu da qua thoi gian quy dinh
                return new RequestInfo(1, now);
            } else if (requestInfo.count < MAX_REQUESTS) {
                // Tang so luong request
                requestInfo.count++;
                return requestInfo;
            } else {
                // Qua so luong request cho phep
                return requestInfo;
            }
        });

        return requestCounts.get(ip).count <= MAX_REQUESTS;
    }

    private static class RequestInfo {
        int count;
        long timestamp;

        RequestInfo(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}
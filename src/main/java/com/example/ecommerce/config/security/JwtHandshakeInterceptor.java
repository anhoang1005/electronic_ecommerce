package com.example.ecommerce.config.security;

import com.example.ecommerce.entities.Users;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.utils.JwtTokenUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtTokenUtils jwtTokenUtils;
    private final UsersRepository usersRepository;

    public JwtHandshakeInterceptor(JwtTokenUtils jwtTokenUtils, UsersRepository usersRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = request.getURI().getQuery().replace("token=", "");
        if (!token.isEmpty()) {
            if (jwtTokenUtils.verifyToken(token)) {
                String email = jwtTokenUtils.extractUsername(token);
                Users users = usersRepository.findUsersEntitiesByEmail(email);
                if(users.getShopOfUsers() != null) {
                    attributes.put("shopCode", users.getShopOfUsers().getShopCode());
                }
                attributes.put("userCode", users.getUserCode());
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}

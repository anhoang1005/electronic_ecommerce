package com.example.ecommerce.exceptions.security;

import com.example.ecommerce.payload.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseBody responseBody = new ResponseBody("",
                ResponseBody.Status.SUCCESS,
                "UNAUTHORIZED",
                ResponseBody.Code.UNAUTHORIZED_REQUEST);

        String json = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(responseBody);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(json);
        log.error("Authentication info error!");
    }
}

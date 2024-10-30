package com.example.ecommerce.exceptions.security;

import com.example.ecommerce.payload.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
        ResponseBody responseBody = new ResponseBody("",
                ResponseBody.Status.SUCCESS,
                "REQUEST NOT FOUND",
                ResponseBody.Code.FORBIDDEN);
        String json = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(responseBody);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        log.warn("Access denied!");
    }
}

package com.example.ecommerce.exceptions;

import com.example.ecommerce.entities.enums.ErrorType;
import com.example.ecommerce.payload.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(ErrorType.U_DONT_HAVE_PERRMISSION_TO_ACCESS_THIS_RESOURCE.toString());
        baseResponse.setCode(String.valueOf(HttpStatus.FORBIDDEN.value()));

        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(baseResponse);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}

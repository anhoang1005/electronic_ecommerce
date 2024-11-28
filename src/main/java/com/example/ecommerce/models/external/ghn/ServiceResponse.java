package com.example.ecommerce.models.external.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private int code;
    private String code_message_value;
    private List<ServiceDto> data;
    private String message;
}

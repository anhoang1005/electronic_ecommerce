package com.example.ecommerce.models.external.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeResponse {
    private int code;
    private String message;
    private TimeDto data;
}

package com.example.ecommerce.payload;

import lombok.Data;

@Data
public class ImportResponse {
    private Integer recordSuccess;
    private Integer recordFailed;
}

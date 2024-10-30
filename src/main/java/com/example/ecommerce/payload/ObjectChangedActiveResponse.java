package com.example.ecommerce.payload;

import lombok.Data;

@Data
public class ObjectChangedActiveResponse {
    private Long objectChangedId;
    private boolean isObjectActive;
}

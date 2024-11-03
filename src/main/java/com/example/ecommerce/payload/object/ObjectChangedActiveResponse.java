package com.example.ecommerce.payload.object;

import lombok.Data;

@Data
public class ObjectChangedActiveResponse {
    private Long objectChangedId;
    private boolean isObjectActive;
}

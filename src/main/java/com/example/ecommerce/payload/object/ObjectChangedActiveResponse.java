package com.example.ecommerce.payload.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectChangedActiveResponse {
    private Long objectChangedId;
    private boolean isObjectActive;
}

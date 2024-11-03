package com.example.ecommerce.payload.object;

import lombok.Data;

@Data
public class UpdateObjectsResponse {
    private Long updatedObjectId;
    private int afterUpdatedAmount;
}

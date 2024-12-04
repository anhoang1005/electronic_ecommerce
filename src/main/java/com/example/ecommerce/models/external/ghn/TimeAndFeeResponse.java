package com.example.ecommerce.models.external.ghn;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeAndFeeResponse {
    private Long leadDate;
    private Integer leadDay;
    private Double fee;
}

package com.example.ecommerce.models.external.ghn;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeDto {
	private Long leadtime;
	private Long order_date;
	private Object leadtime_order;
}

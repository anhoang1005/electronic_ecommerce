package com.example.ecommerce.models.external.ghn;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeRequest {
	private String from_district_id;
	private String from_ward_code;
	private String to_district_id;
	private String to_ward_code;
	private String service_id;
}

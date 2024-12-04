package com.example.ecommerce.models.external.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRequest {
	private String from_district_id;
	private String from_ward_code;
	private String to_district_id;
	private String to_ward_code;
	private String service_id;
}

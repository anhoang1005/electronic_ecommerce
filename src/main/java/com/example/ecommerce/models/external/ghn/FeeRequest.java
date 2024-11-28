package com.example.ecommerce.models.external.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeRequest {
	private String service_id;
	private String insurance_value;
	private String coupon;
	private String from_district_id;
	private String to_district_id;
	private String to_ward_code;
	private String height;
	private String length;
	private String weight;
	private String width;
}

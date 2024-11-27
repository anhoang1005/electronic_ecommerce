package com.example.ecommerce.models.external.ghn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
	private String service_id;
    private String short_name;
    private int service_type_id;
    private String config_fee_id;
    private String extra_cost_id;
    private String standard_config_fee_id;
    private String standard_extra_cost_id;
    private String ecom_config_fee_id;
    private String ecom_extra_cost_id;
    private String ecom_standard_config_fee_id;
    private String ecom_standard_extra_cost_id;
}

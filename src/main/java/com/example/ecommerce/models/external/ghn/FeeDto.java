package com.example.ecommerce.models.external.ghn;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeDto {
	private Double  total;
    private Double service_fee;
    private Double insurance_fee;
    private Double pick_station_fee;
    private Double coupon_value;
    private Double r2s_fee;
    private Double return_again;
    private Double document_return;
    private Double double_check;
    private Double cod_fee;
    private Double pick_remote_areas_fee;
    private Double deliver_remote_areas_fee;
    private Double cod_failed_fee;
}

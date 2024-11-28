package com.example.ecommerce.service;

import com.example.ecommerce.models.external.ghn.FeeRequest;
import com.example.ecommerce.models.external.ghn.FeeResponse;
import com.example.ecommerce.models.external.ghn.TimeRequest;
import com.example.ecommerce.models.external.ghn.TimeResponse;
import com.example.ecommerce.payload.ResponseBody;

public interface GHNService {
	
	//Lay danh sach toan bo cac thanh pho
	ResponseBody getAllProvince();
	
	//Lay danh sach toan bo cac huyen cua thanh pho
	ResponseBody getAllDistrict(String provinceId);
	
	//Lay danh sach toan bo cac xa cua huyen
	ResponseBody getAllWard(String districtId);
	
	//Lay danh sach cac dich vu van chuyen
	ResponseBody getService(Integer to_district);

	//Lay tien ship hang
	FeeResponse getFee(FeeRequest req);

	//Lay thoi gian giao hang du kien
	TimeResponse getTime(TimeRequest req);
}

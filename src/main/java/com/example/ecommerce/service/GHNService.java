package com.example.ecommerce.service;

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
	
//	//Lay tien ship hang
//	ResponseBody getFee(FeeGHNRequest feeRequest);
//
//	//Lay thoi gian giao hang du kien
//	ResponseBody getTime(TimeGHNRequest ghnRequest);
}

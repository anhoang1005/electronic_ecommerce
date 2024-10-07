package com.example.ecommerce.service;

import com.example.ecommerce.payload.ResponseBody;

import java.io.UnsupportedEncodingException;

public interface VnPayService {

	ResponseBody createPayment(long total, String orderType) throws UnsupportedEncodingException;

}

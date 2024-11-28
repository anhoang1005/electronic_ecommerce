package com.example.ecommerce.service.implement;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.external.ghn.FeeRequest;
import com.example.ecommerce.models.external.ghn.FeeResponse;
import com.example.ecommerce.models.external.ghn.TimeRequest;
import com.example.ecommerce.models.external.ghn.TimeResponse;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.GHNService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class IGHNService implements GHNService {
	private final String ghnToken;
	private final String shopId;
	private final String shopDistrict;
	private final String shopWard;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public IGHNService(@Value("${ghn.privateToken}") String ghnToken,
					   @Value("${ghn.shopID}") String shopId,
					   @Value("${ghn.shopDistrict}")String shopDistrict,
					   @Value("${ghn.shopWard}")String shopWard,
					   RestTemplate restTemplate,
					   ObjectMapper objectMapper) {
		this.ghnToken = ghnToken;
		this.shopId = shopId;
		this.shopDistrict = shopDistrict;
		this.shopWard = shopWard;
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public ResponseBody getAllProvince() {
		String apiUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("token", ghnToken);

			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers),
					String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
				return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.error("Get province error" + e.getMessage());
			throw new RequestNotFoundException("Request not found!");
		}
		return null;
	}

	@Override
	public ResponseBody getAllDistrict(String proviceId) {
		String apiUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
		String urlWithParams = apiUrl + "?province_id=" + proviceId;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("token", ghnToken);
			ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET,
					new HttpEntity<>(headers), String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
				return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.error("Get district error" + e.getMessage());
			throw new RequestNotFoundException("Request not found!");
		}
		return null;
	}

	@Override
	public ResponseBody getAllWard(String districtId) {
		String apiUrl = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";
		String urlWithParams = apiUrl + "?district_id=" + districtId;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("token", ghnToken);
			ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET,
					new HttpEntity<>(headers), String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
				return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.error("Get ward error" + e.getMessage());
			throw new RequestNotFoundException("Request not found!");
		}
		return null;
	}

	@Override
	public ResponseBody getService(Integer to_district) {
		String urlWithParams = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services"
				+ "?shop_id=" + shopId
				+ "&from_district=" + shopDistrict
				+ "&to_district=" + to_district;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("token", ghnToken);
			ResponseEntity<String> response = restTemplate.exchange(urlWithParams, HttpMethod.GET,
					new HttpEntity<>(headers), String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
				return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
			}
		} catch (Exception e) {
			throw new RequestNotFoundException("Request not found!");
		}
		return null;
	}

	@Override
	public FeeResponse getFee(FeeRequest req) {
		try {
			String apiParam = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee" + "?service_id="
					+ req.getService_id() + "&insurance_value=" + req.getInsurance_value()
					+ "&from_district_id=" + shopDistrict + "&to_district_id=" + req.getTo_district_id()
					+ "&to_ward_code=" + req.getTo_ward_code() + "&height=" + req.getHeight() + "&length="
					+ req.getLength() + "&weight=" + req.getWeight() + "&width=" + req.getWidth();
			HttpHeaders headers = new HttpHeaders();
			headers.add("token", ghnToken);
			ResponseEntity<String> response = restTemplate.exchange(apiParam, HttpMethod.GET, new HttpEntity<>(headers),
					String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				return objectMapper.readValue(response.getBody(), FeeResponse.class);
			} else {
				throw new RequestNotFoundException("ERROR");
			}
		} catch (Exception e){
			log.error("Get fee error" + e.getMessage());
			throw new RequestNotFoundException("ERROR");
		}
	}

	@Override
	public TimeResponse getTime(TimeRequest req) {
		try {
			String apiParam = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime"
					+ "?from_district_id=" + shopDistrict
					+ "&from_ward_code=" + shopWard
					+ "&to_district_id=" + req.getTo_district_id()
					+ "&to_ward_code=" + req.getTo_ward_code()
					+ "&service_id=" + req.getService_id();
			HttpHeaders headers = new HttpHeaders();
			headers.add("token", ghnToken);
			ResponseEntity<String> response = restTemplate.exchange(apiParam, HttpMethod.GET, new HttpEntity<>(headers),
					String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				return objectMapper.readValue(response.getBody(), TimeResponse.class);
			} else {
				throw new RequestNotFoundException("ERROR");
			}
		} catch (Exception e){
			log.error("Get time error" + e.getMessage());
			throw new RequestNotFoundException("ERROR");
		}
	}
}

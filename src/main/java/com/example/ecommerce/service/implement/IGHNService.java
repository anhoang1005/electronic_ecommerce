package com.example.ecommerce.service.implement;

import com.example.ecommerce.exceptions.request.RequestNotFoundException;
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

	@Value("${ghn.privateToken}")
	private String ghnToken;

	@Value("${ghn.shopID}")
	private String shopId;

	@Value("${ghn.shopDistrict}")
	private String shopDistrict;

	private final RestTemplate restTemplate;
	public IGHNService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
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
}

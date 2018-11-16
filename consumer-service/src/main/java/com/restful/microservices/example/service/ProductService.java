package com.restful.microservices.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.restful.microservices.example.response.Product;

@Service
public class ProductService {

	private Gson gson = new Gson();
	private Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Autowired
	private RestTemplate restTemplate;

	public Object getAllProducts() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Accept", "application/json");

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost").port(8000)
				.path("/db/listproducts");

		HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);

		Object productsObject = null;

		try {

			logger.info("Making a Rest GET Call Towards URL={}", builder.toUriString());

			productsObject = (Object) restTemplate
					.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, String.class).getBody();

		} catch (Exception e) {
			logger.error("Encountered an error when trying to get a Products ", e.getMessage());
		}
		return productsObject;
	}

	public Object addProduct(Product product) {
	
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost").port(8000)
					.path("/db/addproduct");
			
			HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(product), headers);
			
			ResponseEntity<String> answer = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
			
			logger.info(String.valueOf(answer.getStatusCodeValue()));
		} catch (RestClientException e) {
			logger.error("Encountered an error when trying to Add a Product ", e.getMessage());

		}
		
		return this.getAllProducts();
	}

	public Object removeProduct(Product product) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(product), headers);
		String answer = restTemplate.postForObject("http://localhost:8000/db/deleteproduct", entity, String.class);
		System.out.println(answer);
		return this.getAllProducts();
	}

}

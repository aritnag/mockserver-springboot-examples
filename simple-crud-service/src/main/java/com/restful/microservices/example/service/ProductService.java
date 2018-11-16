package com.restful.microservices.example.service;

import java.util.List;

import com.restful.microservices.example.response.Product;
import com.restful.microservices.example.service.response.ProductResponse;

public interface ProductService {

	public ProductResponse getProduct(Long id);
	public Long saveProduct(Product product);
	public boolean deleteProduct(Product product);
	public List<ProductResponse> listAllProducts();
	public ProductResponse getProduct(String id);
}

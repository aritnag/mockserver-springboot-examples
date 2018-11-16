package com.restful.microservices.example.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.restful.microservices.example.repository.ProductRepository;
import com.restful.microservices.example.response.Product;
import com.restful.microservices.example.service.ProductService;
import com.restful.microservices.example.service.response.ProductResponse;



@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	Environment environment;

	@Override
	public ProductResponse getProduct(Long id) {
		com.restful.microservices.example.entity.Product prod = prodRepo.getOne(id);
		ProductResponse returnProd = new ProductResponse();
		BeanUtils.copyProperties(prod, returnProd);
		String deployedPort = (deployedPort = environment.getProperty("server.port")) == null ? deployedPort
				: environment.getProperty("local.server.port");
		returnProd.setPort(Integer.parseInt(deployedPort));
		return returnProd;
	}

	@Override
	public Long saveProduct(Product product) {
		com.restful.microservices.example.entity.Product prod = new com.restful.microservices.example.entity.Product();
		BeanUtils.copyProperties(product, prod);
		prod = prodRepo.save(prod);
		return prod.getId();
	}

	private com.restful.microservices.example.entity.Product checkifProductPresent(Product product) {
		List<com.restful.microservices.example.entity.Product> existingProducts = prodRepo.findAll();
		for (com.restful.microservices.example.entity.Product prd : existingProducts) {
			if (null != product.getProductName() && prd.getProductName().equalsIgnoreCase(product.getProductName())
					&& null != product.getProductType()
					&& prd.getProductType().equalsIgnoreCase(product.getProductType()))
				return prd;
			String deployedPort = (deployedPort = environment.getProperty("server.port")) == null ? deployedPort
					: environment.getProperty("local.server.port");
			prd.setPort(Integer.parseInt(deployedPort));
		}
		return null;
	}

	@Override
	public boolean deleteProduct(Product product) {
		com.restful.microservices.example.entity.Product prod = checkifProductPresent(product);
		if (null != prod) {
			prodRepo.delete(prod);
			return true;
		}
		return false;
	}

	@Override
	public List<ProductResponse> listAllProducts() {
		// List<ProductResponse> prods = new ArrayList<ProductResponse>();
		List<com.restful.microservices.example.entity.Product> products = prodRepo.findAll();
		List<ProductResponse> prods = products.stream().map(temp -> {
			ProductResponse obj = new ProductResponse();
			obj.setProductId(String.valueOf(temp.getId()));
			obj.setProductName(temp.getProductName());
			obj.setProductType(temp.getProductType());
			return obj;
		}).collect(Collectors.toList());
		return prods;
	}

	@Override
	public ProductResponse getProduct(String id) {
		ProductResponse response = null;
		if (!StringUtils.isEmpty(id)) {
			com.restful.microservices.example.entity.Product prd = prodRepo.getOne(Long.parseLong(id));
			response = new ProductResponse();
			BeanUtils.copyProperties(prd, response);
			response.setProductId(String.valueOf(prd.getId()));
		}
		return response;
	}
}

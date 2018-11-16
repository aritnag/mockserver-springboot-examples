package com.restful.microservices.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restful.microservices.example.response.Product;
import com.restful.microservices.example.service.ProductService;


@RestController
@RequestMapping("/listproducts")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@RequestMapping(value = { "/getAllProducts" }, method = RequestMethod.GET)
	public Object getAllProducts() {
		return productService.getAllProducts();
	}
	@RequestMapping(value = { "/addproduct" }, method = RequestMethod.POST)
	public Object addProduct(@RequestBody Product product) {
		return productService.addProduct(product);
	}

	@RequestMapping(value = { "/remove" }, method = RequestMethod.POST)
	public Object removeProduct(@RequestBody Product product) {
		return productService.removeProduct(product);
	}
}

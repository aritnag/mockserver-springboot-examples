package com.restful.microservices.example.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restful.microservices.example.response.Product;
import com.restful.microservices.example.service.ProductService;
import com.restful.microservices.example.service.response.ProductResponse;

@RestController
@RequestMapping("/db")
public class ProductServiceDatabaseController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	Environment environment;

	@RequestMapping(value = { "/listproducts" }, method = RequestMethod.GET)
	public Collection<Resource<ProductResponse>> listAllProducts() {
		List<ProductResponse> listProducts = productService.listAllProducts();
		listProducts.forEach(p -> p.setPort(Integer.parseInt(Optional.ofNullable(environment.getProperty("server.port"))
				.orElse(environment.getProperty("local.server.port")))));
		List<Resource<ProductResponse>> resources = listProducts.stream().map(temp -> getAlbumResource(temp))
				.collect(Collectors.toList());
		return resources;
	}

	private Resource<ProductResponse> getAlbumResource(ProductResponse prodResponse) {
		Resource<ProductResponse> resource = new Resource<ProductResponse>(prodResponse);
		// Link to Product
		resource.add(linkTo(methodOn(ProductServiceDatabaseController.class).getProduct(prodResponse.getProductId()))
				.withSelfRel());
		return resource;
	}

	@RequestMapping(value = { "/addproduct" }, method = RequestMethod.POST)
	public Object addProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

	@RequestMapping(value = { "/deleteproduct" }, method = RequestMethod.POST)
	public Object deleteProduct(@RequestBody Product product) {
		return productService.deleteProduct(product);
	}

	@RequestMapping(value = { "/getproduct/{id}" }, method = RequestMethod.GET, produces = { "application/hal+json" })
	public Resource<ProductResponse> getProduct(@PathVariable String id) {
		ProductResponse product = productService.getProduct(id);
		product.setPort(Integer.parseInt(Optional.ofNullable(environment.getProperty("server.port"))
				.orElse(environment.getProperty("local.server.port"))));
		Resource<ProductResponse> result = new Resource(product);
		result.add(linkTo(methodOn(ProductServiceDatabaseController.class).listAllProducts()).withRel("allProducts"));
		result.add(linkTo(methodOn(ProductServiceDatabaseController.class).getProduct(id)).withSelfRel());
		return result;
	}
}

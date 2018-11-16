package com.restful.microservices.example;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import com.restful.microservices.example.response.Product;

public class Expectations {

	private static Gson gson = new Gson();

	public static void createDefaultExpectations(ClientAndServer mockServer) {

		// POST
	 	addProduct(mockServer);

		// GET
		getProduct(mockServer);

		// Remove
		 removeProduct(mockServer);

	}

	private static void removeProduct(ClientAndServer mockServer) {
		Product product = createProductObjectToPut();
		JsonBody body = new JsonBody(gson.toJson(product));
		
		mockServer.when(request().withMethod("POST")
				.withHeader("Content-Type", "application/json").withPath("/db/deleteproduct")
				.withBody(body))
				.respond(response().withStatusCode(200).withBody(body));

	}

	private static void getProduct(ClientAndServer mockServer) {
		Product   products [] = new Product[1];
		products[0]=createProductObjectToPut();
	
		mockServer.when(request().withMethod("GET")
				.withHeader("Accept", "application/json").withPath("/db/listproducts"))
				.respond(response().withStatusCode(200).withBody(gson.toJson(products)));

	}

	private static void addProduct(ClientAndServer mockServer) {
		Product   products [] = new Product[1];
		products[0]=createProductObjectToPut();
		
		mockServer.when(request().withMethod("POST")
				.withHeader("Content-Type", "application/json").withPath("/db/addproduct")
				.withBody(gson.toJson(createProductObjectToPut())))
				.respond(response().withStatusCode(200).withBody(gson.toJson(products)));
	}

	public static Product createProductObjectToPut() {
		Product product = new Product();
		product.setPort(1000);
		product.setProductId("1000");
		product.setProductName("Test Product");
		product.setProductType("Test Product Type");
		return product;
	}

}

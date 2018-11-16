package com.restful.microservices.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.gson.Gson;
import com.restful.microservices.example.response.Product;
import com.restful.microservices.example.service.ProductService;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@WebAppConfiguration
public class ProductServiceApplicationTests extends BaseTest{

	@Autowired
	private ProductService productService;
	
	private Gson gson = new Gson();


	private Appender mockAppender;

	@Before
	public void setupClient() {

		mockAppender = Mockito.mock(ConsoleAppender.class);

		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(mockAppender);
		logger.setLevel(Level.ERROR);
	}

	@After
	public void teardown() {
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.detachAppender(mockAppender);
	}

	@Test
	public void shouldGetAllProducts() {

		Object response = productService.getAllProducts();
		Product[] returnedProduct = gson.fromJson((String) response, Product[].class);
		Assert.assertEquals(returnedProduct[0].getProductId(), "1000");
		Assert.assertEquals(returnedProduct[0].getProductName(), "Test Product");
	}
	
	@Test
	public void shouldPOSTNewProduct() {

		Object response = productService.addProduct(Expectations.createProductObjectToPut());
		Product[] returnedProduct = gson.fromJson((String) response, Product[].class);
		Assert.assertEquals(returnedProduct[0].getProductId(), "1000");
		Assert.assertEquals(returnedProduct[0].getProductName(), "Test Product");
	}

}

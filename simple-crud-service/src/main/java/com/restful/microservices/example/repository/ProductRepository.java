package com.restful.microservices.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restful.microservices.example.entity.Product;

@Repository
public interface ProductRepository
		extends JpaRepository<com.restful.microservices.example.entity.Product,Long> {
	
//	Product findByNameAndType(String productName,String productType);

}

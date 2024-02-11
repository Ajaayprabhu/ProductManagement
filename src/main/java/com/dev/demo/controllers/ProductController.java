package com.dev.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.demo.models.Product;
import com.dev.demo.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	public ResponseEntity<?> getAllProducts() {
		return productService.getAllProducts();
	}

	@PostMapping("/products")
	public ResponseEntity<?> saveProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

	@PutMapping(path = "/products/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable(value = "productId") Integer productId,
			@RequestBody Product product) {
		return productService.updateProduct(productId, product);
	}

	@PutMapping("/products/updatePrice/{productId}")
	public ResponseEntity<?> updateProductPrice(@PathVariable Integer productId,
			@RequestParam(required = true) String type, @RequestParam(required = true) double value) throws JsonProcessingException {
		return  productService.updateProductPrice(productId, type, value);
	}

	@DeleteMapping(value = "/products/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
		return productService.deleteProductById(productId);
	}
	
}

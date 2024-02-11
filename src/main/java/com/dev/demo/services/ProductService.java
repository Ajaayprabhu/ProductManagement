package com.dev.demo.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.dev.demo.models.Product;
import com.dev.demo.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public ResponseEntity<?> saveProduct(@RequestBody Product product) {
		try {
			Product newProduct = productRepository.save(product);
			return new ResponseEntity<>(newProduct,HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println("Exception occurred while creating product " + e.getMessage());
			return new ResponseEntity<>("Error while creating product", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	public ResponseEntity<?> getAllProducts() {
		try {

			List<Product> productList = productRepository.findAll();

			if (productList == null || productList.isEmpty())
				return new ResponseEntity<>("No products found", HttpStatus.BAD_REQUEST);

			return ResponseEntity.ok(productRepository.findAll());
		} catch (Exception e) {
			System.out.println("Exception occured while getting all products "+ e.getMessage());
			return new ResponseEntity<>("Error while getting all products", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	public ResponseEntity<?> updateProduct(Integer id, Product updatedProduct) {

		try {
			if (id == null) {
				throw new IllegalArgumentException("ID cannot be null");
			}
			Product product = productRepository.findById(id).orElse(null);

			if (product == null)
				return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);

			product.setName(updatedProduct.getName());
			product.setPrice(updatedProduct.getPrice());
			product.setQuantity(updatedProduct.getQuantity());
			Product savedEntity = productRepository.save(product);
			return ResponseEntity.ok(savedEntity);
		} catch (Exception e) {
			System.out.println("Exception occured while updating products " + e.getMessage());
			return new ResponseEntity<>("Error while updating product", HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	public ResponseEntity<?> deleteProductById(Integer id) {
		try {

			if (id == null) {
				throw new IllegalArgumentException("ID cannot be null");
			}

			productRepository.deleteById(id);
			return ResponseEntity.ok("Product Deleted Successfully");
		} catch (Exception e) {
			System.out.println("Exception occured while deleting product "+  e.getMessage());
			return new ResponseEntity<>("Error while deleting product", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	public ResponseEntity<?> updateProductPrice(Integer productId, String type, double value) {

		try {

			Product product = productRepository.findById(productId).orElse(null);

			if (product == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
			}

			if ("discount".equalsIgnoreCase(type)) {
				double discountPercentage = value;
				double discountedPrice = product.getPrice() * (1 - (discountPercentage / 100));
				product.setPrice(discountedPrice);
			} else if ("tax".equalsIgnoreCase(type)) {
				double taxRate = value;
				double taxedPrice = product.getPrice() * (1 + (taxRate / 100));
				product.setPrice(taxedPrice);
			} else {
				return ResponseEntity.badRequest().body("Invalid type provided. Please choose 'discount' or 'tax'.");
			}

			productRepository.save(product);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			System.out.println("Exception occured while updating product price " + e.getMessage());
			return new ResponseEntity<>("Error while updating product price", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}

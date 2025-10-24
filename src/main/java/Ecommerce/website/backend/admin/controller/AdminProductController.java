package Ecommerce.website.backend.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.admin.service.AdminProductService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
	
	@Autowired
	AdminProductService service;
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> request) {
		try {
			
			//Extracting requestBody to variables
			
			
			String name = (String) request.get("name");
			if(service.productContain(name)) {
				return ResponseEntity.badRequest().body(Map.of("error", "Product exist"));
			}
			String description = (String) request.get("description");
			int stock = (int) request.get("stock");
			Double price = Double.valueOf(String.valueOf(request.get("price")));
			String imageUrl = (String) request.get("imageUrl");
			int categoryId = (int) request.get("categoryId"); // have a doubt is it category or categoryId
			
			Product newP =service.addProduct(name, description, price, stock, imageUrl, categoryId);
			
			Map<String, Object> response = new HashMap<>();
			Map<String,Object> product = new HashMap<>();
			product.put("name", newP.getName());
			product.put("category",newP.getCategory());
			product.put("stock",newP.getStock());
			product.put("productId", newP.getProductId());
			product.put("price", newP.getPrice());
			product.put("description", newP.getDescription());
			product.put("createdAt", newP.getCreatedAt());
			
			response.put("product", product);
			response.put("imageUrl", newP.getImage());
			
			return ResponseEntity.ok(response);
			
			
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("error", "Internal Error"));
		}
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProduct(@RequestBody Map<String, Object> request) {
		int id = (int) request.get("productId");
		try {
			service.deleteProductById(id);
			return ResponseEntity.ok("Product Deleted.");
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("error", "Internal Server Error"));
		}
	}
}

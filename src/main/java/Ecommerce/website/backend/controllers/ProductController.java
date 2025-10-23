package Ecommerce.website.backend.controllers;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.Entities.Category;
import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.Entities.ProductImage;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")


@RestController
@RequestMapping("/api/customer/products")
public class ProductController {
	
	@Autowired
	public ProductService productService;
	
	
	@GetMapping
	public ResponseEntity<Map<String,Object>> getProducts(@RequestParam(name="category", required = false) String categoryName, HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			if(user == null) {
				return ResponseEntity.status(401).body(Map.of("error","Unauthorized Access"));
			}
			
			List<Product> productsList = productService.getProductByCategory(categoryName); 
			
			Map<String, Object> response = new HashMap<>();
			
			Map<String,String> userInfo = new HashMap<>();
			
			userInfo.put("name", user.getUsername());
			userInfo.put("role",user.getRole().name());
			response.put("userInfo", userInfo);
			
			List<Map<String,Object>> productsMapList = new ArrayList<>();
			for(Product p: productsList) {
				Map<String,Object> products = new HashMap<>();
				products.put("product_id", p.getProductId());
				products.put("name", p.getName());
				products.put("description", p.getDescription());
				products.put("price", p.getPrice());
				products.put("stock", p.getStock());
				
				List<String> images = productService.getProductImages(p.getProductId());
				products.put("images", images);
				
				productsMapList.add(products);
				
			}
			
			response.put("products", productsMapList);
			
			return ResponseEntity.ok(response);
			
		}
		catch(RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		
		
	}
}

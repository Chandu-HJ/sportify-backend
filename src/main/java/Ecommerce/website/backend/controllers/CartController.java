package Ecommerce.website.backend.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.Entities.Cart;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;
import Ecommerce.website.backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {
	
	@Autowired
	private CartService service;

	
	
	
	@GetMapping("/count")
	public ResponseEntity<Map<String, Object>> getItemsCount(HttpServletRequest request) {
		
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			Map<String,Object> response = new HashMap<>();
			 response.put("count",service.getItemsCount(user.getUsername()));
			 
			 return ResponseEntity.ok(response);
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addItems(@RequestBody Map<String, Object> request) {
		try {
			String username =(String) request.get("username");
			int productId = (int) request.get("productId");
			
			
			
			if(request.containsKey("quantity")) {
				int quantity = (int)request.get("quantity");
				service.addItem(username, productId, quantity);
				return ResponseEntity.status(HttpStatus.CREATED).build();
				
			}
			service.addItem(username, productId, 1);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error" , e.getMessage() ));
		}
		
	}
	
	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getAllItems(HttpServletRequest request) {
		try {
			User user = (User)request.getAttribute("authenticatedUser");
			Map<String, Object> response = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			
			List<Cart> items = service.getAllItems(user.getId());
			BigDecimal overAllTotal = BigDecimal.ZERO;
			for(Cart c: items) {
				Map<String, Object> map = new HashMap<>();
				map.put("productId", c.getProduct().getProductId());
				map.put("pricePerItem", c.getProduct().getPrice());
				map.put("quantity", c.getQuantity());
				map.put("totalPrice", c.getProduct().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
				overAllTotal = overAllTotal.add((BigDecimal) map.get("totalPrice"));
				list.add(map);
			}
			
			response.put("username", user.getUsername());
			response.put("items", list);
			response.put("grandTotal", overAllTotal);
			
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteItem(@RequestBody Map<String, Object> request) {
		String userName = (String) request.get("username");
		int productId = -1;
		if(request.containsKey("productId"))
			productId = (int) request.get("productId");
		
		try {
			if(productId == -1) {
				service.deleteAll(userName);
			}
			else {
				service.delete(userName, productId);
			}
			return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e) {
			String mess = e.getMessage();
			if(mess.equals("user")) {
				return ResponseEntity.status(400).body(Map.of("error", "User not found with username '" + userName + "'."));
			}
			if(mess.equals("product")) {
				return ResponseEntity.status(404).body(Map.of("error", "Product not found with Id: " + productId));
			}
			if(mess.equals("cart")) {
				return ResponseEntity.status(404).body(Map.of("error", "No Such Items in cart"));

			}
			
			return ResponseEntity.status(500).body(Map.of("error", "Unexpected Error  " + e.getMessage()));

		}
	}
	
	
	
	
}

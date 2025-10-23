package Ecommerce.website.backend.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;
import Ecommerce.website.backend.service.OrdersService;
import Ecommerce.website.backend.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/customer/orders")
public class OrdersController {
	
	@Autowired
	public OrdersService service;
	
	@Autowired
	private UsersService userService;
	
	@GetMapping
	public ResponseEntity<?> getOrders(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			if(user == null) {
				return ResponseEntity.status(401).body(Map.of("error", "user not authenticated"));
			}
			
			String username = (String) requestBody.get("username");
			
			Map<String, Object> response = service.getOrderItems(username);
			return ResponseEntity.ok(response);
			
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(400).body(Map.of("error" , e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Internal Server error"));
		}
	}
}

package Ecommerce.website.backend.admin.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.Entities.Role;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.admin.service.AdminUserService;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {
	
	@Autowired
	private AdminUserService service;
	
	@PostMapping("/getbyid")
	public ResponseEntity<?> getById(@RequestBody Map<String,Object> request) {
		try {
			User user = service.getUser((int) request.get("userId"));
			return ResponseEntity.ok(user);
			
//			Object userIdObj = request.get("userId");
//	        if (userIdObj == null) {
//	            return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
//	        }
//
//	        int userId;
//	        if (userIdObj instanceof Integer) {
//	            userId = (Integer) userIdObj;
//	        } else if (userIdObj instanceof String) {
//	            userId = Integer.parseInt((String) userIdObj);
//	        } else {
//	            return ResponseEntity.badRequest().body(Map.of("error", "Invalid userId type"));
//	        }

//	        User user = service.getUser(userId);
//	        return ResponseEntity.ok(user);
		}
		catch(IllegalArgumentException e ) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("error", "Internal Server Error"));

		}
	}
	
	@PutMapping("/modify") 
	public ResponseEntity<?> modifyUser(@RequestBody Map<String, Object> request) {
		try {
			Object userIdObj = request.get("userId");
			int userId;
			if(userIdObj instanceof Number) {
				userId = (Integer) userIdObj;
			}
			else if(userIdObj instanceof String){
				userId = Integer.parseInt((String) userIdObj);
			}
			else {
				throw new IllegalArgumentException("userId is incorrect");
			}
			
			String name = (String) request.get("username");
			String email = (String) request.get("email");
			Role role = Role.valueOf((String)request.get("role"));
			
			service.modifyUser(userId, name, email, role);
			
			
			return ResponseEntity.ok(Map.of("message","User Modified Success"));
		}
		catch(IllegalArgumentException e ) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("error", "Internal Server Error"));

		}
		
	}
}

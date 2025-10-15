package Ecommerce.website.backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.service.Registration;

@RestController
@RequestMapping("/api/users")
public class RegistrationController {
	
	private final Registration service;
	
	@Autowired
	public RegistrationController(Registration service) {
		this.service = service;
	}
	
	@RequestMapping("/")
	public String printHello() {
		return "Hello";
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			User registeredUser = service.registerUser(user);
			return ResponseEntity.ok(Map.of(
					"message","User Registeres Successfully",
					"user",registeredUser
					));
		}
		catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
		}
	}
}

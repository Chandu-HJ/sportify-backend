package Ecommerce.website.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;

@Service
public class Registration {
	
	private final UsersRepo repo;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public Registration(UsersRepo repo, BCryptPasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	public User registerUser(User user) {
		
		if(repo.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("User Name is Already Taken!!");
		}
		
		if(repo.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already Registered!!");
			
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return repo.save(user);
	}

}

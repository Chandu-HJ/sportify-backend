package Ecommerce.website.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;

@Service
public class UsersService {
	
	@Autowired
	UsersRepo repo;
	public User getUserByUserName(String userName) {
		return repo.findByUsername(userName).orElseThrow(()-> new IllegalArgumentException("user Not found"));
	}
}

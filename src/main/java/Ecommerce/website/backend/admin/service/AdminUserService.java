package Ecommerce.website.backend.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.Role;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;

@Service
public class AdminUserService {

		@Autowired
		UsersRepo userRepo;
		
		public User getUser(int userId) {
			return userRepo.findById(userId).orElseThrow(()-> new IllegalArgumentException("User not found"));
		}
		
		public void modifyUser(int id, String name, String email, Role role) {
			if(!userRepo.existsById(id)) {
				throw new IllegalArgumentException("UserId not exist");
			}
			
			User user = userRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("User not found"));
			user.setUsername(name);
			user.setEmail(email);
			user.setRole(role);
			
			userRepo.save(user);
		}
}

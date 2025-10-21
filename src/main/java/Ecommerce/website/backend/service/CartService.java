package Ecommerce.website.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import Ecommerce.website.backend.Entities.Cart;
import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.CartRepo;
import Ecommerce.website.backend.Repos.ProductRepo;
import Ecommerce.website.backend.Repos.UsersRepo;

@Service
public class CartService {

		@Autowired
		private CartRepo cartRepo;
		
		@Autowired
		private UsersRepo usersRepo;
		
		@Autowired
		private ProductRepo productRepo;
		
		public int getItemsCount(String userName) {
			
			Optional<User> user = usersRepo.findByUsername(userName);
			
			if(!user.isEmpty()) {
				return cartRepo.countTotalItems(user.get().getId());
				
			}
			else {
				throw new IllegalArgumentException("Invalid User");
			}
	
		}
		
		public void addItem(String userName, int productId, int quantity) {
			User user = usersRepo.findByUsername(userName).orElseThrow(()-> new IllegalArgumentException("User not found."));
			
			
			Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not found in Products Table"));
			
			Optional<Cart> cart = cartRepo.findByUserAndProduct(user.getId(), product.getProductId());
			
			if(cart.isEmpty()) {
				Cart newCartItem = new Cart(user, product, quantity);
				cartRepo.save(newCartItem);
			}
			else {
				Cart cartItem = cart.get();
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				cartRepo.save(cartItem);
			}
						
			
		}
		
		
		
		public List<Cart> getAllItems(int userId) {
//			User user = usersRepo.findByUsername(userName).orElseThrow(()-> new IllegalArgumentException("User not found!"));
			return cartRepo.findAllByUserId(userId);
		}
		
		public void delete(String userName, int productId) throws Exception {
			User user = usersRepo.findByUsername(userName).orElseThrow(()-> new IllegalArgumentException("user"));
			Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("product"));
			Optional<Cart> cart = cartRepo.findByUserAndProduct(user.getId(),product.getProductId());
			
			if(cart.isPresent()) {
				Cart c = cart.get();
				
				c.setQuantity(c.getQuantity()-1);
				if(c.getQuantity() == 0) {
					cartRepo.delete(c);
				}
				else {
					cartRepo.save(c);
				}
			}
			else {
				 throw new IllegalArgumentException("cart");
			}
			
		}
		
		public void deleteAll(String userName) {
			User user = usersRepo.findByUsername(userName).orElseThrow(()-> new IllegalArgumentException("User not found with the username '" + userName + "'."));
		
			cartRepo.deleteAllByUserId(user.getId());
		
			
		}

}

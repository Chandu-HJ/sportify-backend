package Ecommerce.website.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Repos.UsersRepo;
import Ecommerce.website.backend.Entities.OrderItem;
import Ecommerce.website.backend.Entities.ProductImage;
import Ecommerce.website.backend.Entities.Status;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.OrderItemsRepo;
import Ecommerce.website.backend.Repos.ProductImageRepo;


@Service
public class OrdersService {

	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	OrderItemsRepo orderItemsRepo;
	
	@Autowired
	ProductImageRepo imageRepo;
	
	public Map<String,Object> getOrderItems(String userName) {
		User user = (User) usersRepo.findByUsername(userName).orElseThrow(()-> new IllegalArgumentException("User not found"));
		Map<String, Object> response = new HashMap<>();
		response.put("username", user.getUsername());
		response.put(userName, user.getRole());
		List<Map<String,Object>> itemsList = new ArrayList<>();
		List<OrderItem> orderItems = orderItemsRepo.findSuccessfullOrderItemsByUserId(user.getId(), Status.SUCCESS);
		for(OrderItem oi: orderItems) {
			Map<String, Object> products = new HashMap<>();
			if(products == null) {
				continue;
			}
			products.put("order_id", oi.getOrder().getOrderId());
			products.put("quantity", oi.getQuantity());
			products.put("total_price",oi.getTotalPrice());
			products.put("description", oi.getProduct().getDescription());
			products.put("product_id", oi.getProduct().getProductId());
			products.put("price_per_unit", oi.getPricePerUnit());
			List<ProductImage> imageUrls = imageRepo.findByProduct_ProductId(oi.getProduct().getProductId());
			String imageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0).getImageUrl();
			products.put("image_url",imageUrl);
			itemsList.add(products);
		}
		
		response.put("products", itemsList);
		
		return response;
				
	}
}

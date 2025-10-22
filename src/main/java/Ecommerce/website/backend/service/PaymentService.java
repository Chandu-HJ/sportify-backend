package Ecommerce.website.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import Ecommerce.website.backend.Entities.Cart;
import Ecommerce.website.backend.Entities.OrderItem;
import Ecommerce.website.backend.Entities.Orders;
import Ecommerce.website.backend.Entities.Status;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.CartRepo;
import Ecommerce.website.backend.Repos.OrderItemsRepo;
import Ecommerce.website.backend.Repos.OrdersRepo;
import Ecommerce.website.backend.Repos.UsersRepo;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	private OrderItemsRepo orderItemsRepo;
	
	private OrdersRepo  ordersRepo;
	
	private CartRepo cartRepo;
	
	@Autowired
	private final UsersRepo userRepo;
	
	@Autowired
	public PaymentService(OrderItemsRepo orderItemsRepo, OrdersRepo ordersRepo, CartRepo cartRepo,UsersRepo userRepo) {
		super();
		this.userRepo = userRepo;
		this.orderItemsRepo = orderItemsRepo;
		this.ordersRepo = ordersRepo;
		this.cartRepo = cartRepo;
	}
	
	@Value("${razorpay.key_id}")
	private String razorpayKeyId;
	
	@Value("${razorpay.key_secret}")
	private String razorpayKeySecret;
	
	@Transactional
	public String createOrder(int userId, BigDecimal totalAmount, List<OrderItem> cartItems) throws RazorpayException {
		//create razorpay client
		RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
		
		//prepare Razorpay order request
		
		var orderRequest = new JSONObject();
		
		orderRequest.put("amount",totalAmount.multiply(BigDecimal.valueOf(100)));
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
		
		com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
		
		Orders order = new Orders();
		
		order.setOrderId(razorpayOrder.get("id"));
		User user = userRepo.findById(userId).orElseThrow(()-> new IllegalArgumentException("Invalid userId"));
		order.setUser(user);
		order.setTotalAmount(totalAmount);
		order.setStatus(Status.PENDING);
		order.setCreatedAt(LocalDateTime.now());
		ordersRepo.save(order);
		
		return razorpayOrder.get("id");
		
		
		
	}
	
	@Transactional
	public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, int userId)  {
		try {
			JSONObject attributes = new JSONObject();
			attributes.put("razorpay_order_id", razorpayOrderId);
			attributes.put("razorpay_payment_id", razorpayPaymentId);
			attributes.put("razorpay_signature", razorpaySignature);
			
			boolean isVerified = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
			if(isVerified) {
				Orders order = ordersRepo.findById(razorpayOrderId).orElseThrow(()-> new IllegalArgumentException("Invalid orderId"));
				order.setStatus(Status.SUCCESS);
				order.setUpdatedAt(LocalDateTime.now());
				ordersRepo.save(order);
				
				List<Cart> cart = cartRepo.findAllByUserId(userId);
				for(Cart c: cart) {
					OrderItem item = new OrderItem();
					
					item.setOrder(order);
					item.setPricePerUnit(c.getProduct().getPrice());
					item.setProduct(c.getProduct());
					item.setQuantity(c.getQuantity());
					item.setTotalPrice(c.getProduct().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
					
					orderItemsRepo.save(item);
				}
				
				cartRepo.deleteAllByUserId(userId);
				
				return true;
				
			}
			else {
				return false;
			}
		}
		
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional
	public void saveOrderItems(String orderId, List<OrderItem> items) {
		Orders order = ordersRepo.findById(orderId).orElseThrow(()-> new IllegalArgumentException("invalid OrderId"));
		for(OrderItem item: items) {
			item.setOrder(order);
			orderItemsRepo.save(item);
		}
	}
	
}

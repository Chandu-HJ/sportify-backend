package Ecommerce.website.backend.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;

import Ecommerce.website.backend.Entities.OrderItem;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;
import Ecommerce.website.backend.service.PaymentService;
import Ecommerce.website.backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/customer/payment")
public class PaymentController {
	
	private final PaymentService service;
	
	private final UsersRepo usersRepo;

	@Autowired
	public PaymentController(PaymentService service, UsersRepo usersRepo) {
		super();
		this.service = service;
		this.usersRepo = usersRepo;
	}
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createPayment(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
	    try {
	        User user = (User) request.getAttribute("authenticatedUser");
	        if(user == null) {
	            throw new IllegalArgumentException("User is not Authorized");
	        }

	        // Correct handling of totalAmount
	        Object totalAmountObj = requestBody.get("totalAmount");
	        BigDecimal totalAmount;
	        if (totalAmountObj instanceof Number) {
	            totalAmount = BigDecimal.valueOf(((Number) totalAmountObj).doubleValue());
	        } else {
	            totalAmount = new BigDecimal((String) totalAmountObj);
	        }

	        // Cart items
	        List<Map<String, Object>> itemsRaw = (List<Map<String, Object>>) requestBody.get("cartItems");
	        List<OrderItem> items = itemsRaw.stream().map((item) -> {
	            OrderItem oItem = new OrderItem();
	            oItem.setProduct(productService.getproductById((int) item.get("productId")));

	            Object priceObj = item.get("price");
	            BigDecimal price;
	            if (priceObj instanceof Number) {
	                price = BigDecimal.valueOf(((Number) priceObj).doubleValue());
	            } else {
	                price = new BigDecimal((String) priceObj);
	            }

	            oItem.setPricePerUnit(price);
	            oItem.setQuantity((int) item.get("quantity"));
	            oItem.setTotalPrice(price.multiply(BigDecimal.valueOf(oItem.getQuantity())));
	            return oItem;
	        }).collect(Collectors.toList());

	        String razorpayOrderId = service.createOrder(user.getId(), totalAmount, items);

	        return ResponseEntity.ok(razorpayOrderId);
	    }
	    catch(RazorpayException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(402).body(Map.of("error","RazorPay Conflict"));
	    }
	    catch(Exception e){
	        e.printStackTrace();
	        return ResponseEntity.status(402).body(Map.of("error", e.getMessage()));
	    }
	}

	
	
	@PostMapping("/verify")
	public ResponseEntity<?> verifyPayment(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			if (user == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			                         .body(Map.of("error", "User is not authorized"));
			}

			int userId = user.getId();
			String razorpayOrderId = (String) requestBody.get("razorpayOrderId");
			String razorpayPaymentId = (String) requestBody.get("razorpayPaymentId");
			String razorpaySignature = (String) requestBody.get("razorpaySignature");
			
			if(service.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature, userId)) {
				return ResponseEntity.ok(Map.of("message", "Payment verified Successfully"));
			}
			else {
				return ResponseEntity.status(402).body(Map.of("error", "Payment failed"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(402).body(Map.of("error", e.getMessage()));
		}
	}

	
}

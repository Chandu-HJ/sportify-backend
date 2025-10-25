package Ecommerce.website.backend.admin.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.OrderItem;
import Ecommerce.website.backend.Entities.Orders;
import Ecommerce.website.backend.Entities.Status;
import Ecommerce.website.backend.Repos.OrderItemsRepo;
import Ecommerce.website.backend.Repos.OrdersRepo;

@Service
public class AdminBusinessService {
	
	@Autowired
	private OrdersRepo ordersRepo;
	
	@Autowired
	private OrderItemsRepo oiRepo;
	
	//Method for dailyBusiness
	
	public Map<String, Object> getDailyBusiness(LocalDate date) {
		
//		LocalDateTime start = date.atStartOfDay();
//		LocalDateTime end = date.plusDays(1).atStartOfDay();
		List<Orders> successOrders = ordersRepo.findSuccessOrdersByDate(date, Status.SUCCESS);
		return calculateBusiness(successOrders);
	}
	


	//Method for MonthlyBusiness
	
	public Map<String, Object> getMonthlyBusiness(int month, int year) {
		
		List<Orders> successOrders = ordersRepo.findSuccessOrdersByMonth(month, year, Status.SUCCESS);
		return calculateBusiness(successOrders);
	}
	
	
	
	
//	Method for YearlyBusiness
	public Map<String, Object> getYearlyBusiness(int year) {
		
			List<Orders> successOrders = ordersRepo.findSuccessOrdersByYear(year, Status.SUCCESS);
			return calculateBusiness(successOrders);
	}
	
	
	
	//Method for overAllBusiness
	
	public Map<String, Object> getOverallBusiness() {
		
		List<Orders> successOrders = ordersRepo.findAllSuccessOrders(Status.SUCCESS);
		return calculateBusiness(successOrders);
	}

	
	
//	Method for calculation
	public Map<String,Object> calculateBusiness(List<Orders> orders) {
		BigDecimal totalBusiness = new BigDecimal(0);
		Map<String,BigDecimal> categories = new HashMap<>();
		for(Orders o: orders) {
			totalBusiness = totalBusiness.add(o.getTotalAmount());
			List<OrderItem> oi = oiRepo.findByOrderId(o.getOrderId());
			for(OrderItem item: oi) {
				String categoryName = item.getProduct().getCategory().getCategoryName();
				categories.put(categoryName,categories.getOrDefault(categoryName, BigDecimal.ZERO).add(item.getTotalPrice()));
				
			}
			
		}
		Map<String, Object> response = new HashMap<>();
		response.put("totalBusiness", totalBusiness);
		response.put("categorySales", categories);
		
		return response;
	}
	
}

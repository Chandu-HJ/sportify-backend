package Ecommerce.website.backend.admin.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ecommerce.website.backend.admin.service.AdminBusinessService;

@RestController
@RequestMapping("/api/admin/business")
public class AdminBusinessController {

	@Autowired
	AdminBusinessService service;
	
	@GetMapping("/daily")
	public ResponseEntity<?> getDailyReport(@RequestParam("date") LocalDate date) {
		try {
			Map<String,Object> response = service.getDailyBusiness(date);
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server error");
		}
	}
	
	@GetMapping("/monthly")
	public ResponseEntity<?> getMonthlyReport(@RequestParam("month") int month, @RequestParam("year") int year) {
		try {
			Map<String,Object> response = service.getMonthlyBusiness(month, year);
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server error");
		}
	}
	
	@GetMapping("/yearly")
	public ResponseEntity<?> getYearlyReport(@RequestParam("year") int year) {
		try {
			Map<String,Object> response = service.getYearlyBusiness(year);
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server error");
		}
	}
	
	@GetMapping("/overall")
	public ResponseEntity<?> getDailyReport() {
		try {
			Map<String,Object> response = service.getOverallBusiness();
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal Server error");
		}
	}
}

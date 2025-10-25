package Ecommerce.website.backend.Repos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

import Ecommerce.website.backend.Entities.Orders;
import Ecommerce.website.backend.Entities.Status;

public interface OrdersRepo extends JpaRepository<Orders, String> {
	
//	@Query("select o from Orders o where o.createdAt >= :start and o.createdAt < :end and o.status = :status")
//	public List<Orders> findSuccessOrdersByDate(LocalDateTime start, LocalDateTime end, Status status);
	
	//querry for fetching successfull orders By particular date
	
	@Query("select o from Orders o where date(o.createdAt)=:date and o.status = :status")
	public List<Orders> findSuccessOrdersByDate(@Param("date") LocalDate date, @Param("status") Status status);
	
	
	//querry for fetching successfull orders By particular month
	
	@Query("select o from Orders o where month(o.createdAt) = :month and year(o.createdAt)=:year and o.status=:status")
	public List<Orders> findSuccessOrdersByMonth(int month, int year, Status status);
	
	//querry for fetching successfull orders By particular year
	
	@Query("select o from Orders o where year(o.createdAt)=:year and o.status=:status")
	public List<Orders> findSuccessOrdersByYear(int year,Status status);
	
	//querry for fetching successfull orders 
	
	@Query("select o from Orders o where o.status=:status")
	public List<Orders> findAllSuccessOrders(Status status);
	

}

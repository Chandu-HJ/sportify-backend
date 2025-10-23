package Ecommerce.website.backend.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Ecommerce.website.backend.Entities.OrderItem;
import Ecommerce.website.backend.Entities.Status;

@Repository
public interface OrderItemsRepo extends JpaRepository<OrderItem, Integer> {
	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
	List<OrderItem> findByOrderId(String orderId);
	
//	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.user.userId = :userId AND oi.order.status = :status")
//	List<OrderItem> findSuccessfullOrderItemsByUserId(
//	        @Param("userId") int userId, 
//	        @Param("status") Status status
//	);
	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.user.id = :userId AND oi.order.status = :status")
	List<OrderItem> findSuccessfullOrderItemsByUserId(int userId, Status status);


}

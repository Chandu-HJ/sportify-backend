package Ecommerce.website.backend.Repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Ecommerce.website.backend.Entities.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.product.productId = :productId")
	Optional<Cart> findByUserAndProduct(Integer userId, Integer productId);
	
	@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.user.id = :userId")
	int countTotalItems(int userId);
	
	@Query("SELECT c  FROM Cart c WHERE c.user.id = :userId")
	List<Cart> findAllByUserId(int userId);
	
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Cart c WHERE c.user.id = :userId")
	void deleteAllByUserId(int userId);
}

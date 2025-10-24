package Ecommerce.website.backend.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Ecommerce.website.backend.Entities.JWTToken;
import jakarta.transaction.Transactional;

@Repository
public interface JWTTokenRepo extends JpaRepository<JWTToken, Integer> {
		@Modifying
		@Transactional
		@Query("DELETE JWTToken j WHERE j.user.id=:userId")
		public void deleteByUserId(int userId);
}

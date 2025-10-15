package Ecommerce.website.backend.Repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ecommerce.website.backend.Entities.User;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
}

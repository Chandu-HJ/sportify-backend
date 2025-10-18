package Ecommerce.website.backend.Repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ecommerce.website.backend.Entities.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer>{
	Optional<Category> findByCategoryName(String categoryName);
}

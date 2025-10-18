package Ecommerce.website.backend.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

import Ecommerce.website.backend.Entities.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	List<Product> findByCategory_CategoryId(Integer categoryId);

}

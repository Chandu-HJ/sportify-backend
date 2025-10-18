package Ecommerce.website.backend.Repos;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ecommerce.website.backend.Entities.ProductImage;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Integer> {
		List<ProductImage> findByProduct_ProductId(Integer productId);
}

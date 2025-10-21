package Ecommerce.website.backend.service;

import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.Category;
import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.Entities.ProductImage;
import Ecommerce.website.backend.Repos.CategoryRepo;
import Ecommerce.website.backend.Repos.ProductImageRepo;
import Ecommerce.website.backend.Repos.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ProductImageRepo productImageRepo;
	
	public List<Product> getProductByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = categoryRepo.findByCategoryName(categoryName);
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return productRepo.findByCategory_CategoryId(category.getCategoryId());
			}
			else {
				throw new RuntimeException("category not Found");
			}
		}
		else {
			return productRepo.findAll();
		}
	}
	
	public List<String> getProductImages(Integer productId) {
		List<ProductImage> productImages = productImageRepo.findByProduct_ProductId(productId);
		
		List<String> imageUrls = new ArrayList<>();
		
		for(ProductImage image: productImages) {
			imageUrls.add(image.getImageUrl());
		}
		
		return imageUrls;
	}
	
	public int getStock(Integer productId) {
		Product product = productRepo.findById(productId).orElseThrow (() -> new IllegalArgumentException("Product not found"));
		
		return product.getStock();
	}
	
	
}

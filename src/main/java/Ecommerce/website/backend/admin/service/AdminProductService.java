package Ecommerce.website.backend.admin.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.Category;
import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.Entities.ProductImage;
import Ecommerce.website.backend.Repos.CategoryRepo;
import Ecommerce.website.backend.Repos.ProductImageRepo;
import Ecommerce.website.backend.Repos.ProductRepo;

@Service
public class AdminProductService {
	
	public ProductRepo pRepo;
	private ProductImageRepo pIRepo;
	private CategoryRepo cRepo;
	
	@Autowired
	public AdminProductService(ProductRepo pRepo, ProductImageRepo pIRepo, CategoryRepo cRepo) {
		super();
		this.pRepo = pRepo;
		this.pIRepo = pIRepo;
		this.cRepo = cRepo;
	}
	
	public Product addProduct(String name, String description, double price, int stock, String imageUrl, int categoryId) {
		
		Product newP = pRepo.findByProductName(name);
		if(newP==null) {
			newP = new Product();
		}
//		Product newP = new Product();
		newP.setName(name);
		newP.setDescription(description);
		newP.setPrice(BigDecimal.valueOf(price));
		newP.setStock(stock);
		Category c = cRepo.findById(categoryId).orElseThrow(()-> new IllegalArgumentException("product category not found."));
		newP.setCategory(c);
		ProductImage pImage = new ProductImage();
		
		newP.setImage(pImage);
		newP.setCreatedAt(LocalDateTime.now());
		newP.setUpdatedAt(LocalDateTime.now());
		
		pImage.setImageUrl(imageUrl);
		pImage.setProduct(newP);
		pRepo.save(newP);


		
		return newP;
	}
	
	public boolean productContain(String name) {
		return pRepo.existsByName(name);

	}
	
	public void deleteProductById(int id) {
		if(!pRepo.existsById(id)) {
			throw new IllegalArgumentException("Product doesnt exists.");
		}
		pRepo.deleteById(id);
	}
	
	
}

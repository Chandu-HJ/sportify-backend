package Ecommerce.website.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Ecommerce.website.backend.Entities.Cart;
import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.CartRepo;
import Ecommerce.website.backend.Repos.ProductRepo;
import Ecommerce.website.backend.Repos.UsersRepo;

@Service
public class CartService {

    @Autowired private CartRepo cartRepo;
    @Autowired private UsersRepo usersRepo;
    @Autowired private ProductRepo productRepo;

    public int getItemsCount(String userName) {
        User user = usersRepo.findByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User"));
        return cartRepo.countTotalItems(user.getId());
    }

    public void addItem(String userName, int productId, int quantity) {
        User user = usersRepo.findByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product Not found"));

        Optional<Cart> cartOpt = cartRepo.findByUserAndProduct(user.getId(), product.getProductId());

        if (cartOpt.isEmpty()) {
            cartRepo.save(new Cart(user, product, quantity));
        } else {
            Cart cart = cartOpt.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cartRepo.save(cart);
        }
    }

    public List<Cart> getAllItems(int userId) {
        return cartRepo.findAllByUserId(userId);
    }

    // NEW: Set exact quantity
    public void setQuantity(String username, int productId, int newQty) {
        User user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("user"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product"));

        Optional<Cart> opt = cartRepo.findByUserAndProduct(user.getId(), product.getProductId());
        if (opt.isEmpty()) {
            if (newQty > 0) cartRepo.save(new Cart(user, product, newQty));
            return;
        }

        Cart c = opt.get();
        if (newQty <= 0) {
            cartRepo.delete(c);
        } else {
            c.setQuantity(newQty);
            cartRepo.save(c);
        }
    }

    // NEW: Delete entire row
    public void deleteCompletely(String username, int productId) {
        User user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("user"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product"));
        cartRepo.deleteByUserIdAndProductId(user.getId(), product.getProductId());
    }

    public void deleteAll(String username) {
        User user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        cartRepo.deleteAllByUserId(user.getId());
    }
}

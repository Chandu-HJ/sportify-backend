package Ecommerce.website.backend.controllers;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Ecommerce.website.backend.Entities.Cart;
import Ecommerce.website.backend.Entities.Product;
import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    @Autowired private CartService service;

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getItemsCount(HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("authenticatedUser");
            if (user == null) throw new IllegalArgumentException("Not authenticated");
            int count = service.getItemsCount(user.getUsername());
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItem(@RequestBody Map<String, Object> req) {
        try {
            String username = (String) req.get("username");
            int productId = ((Number) req.get("productId")).intValue();
            int qty = req.containsKey("quantity") ? ((Number) req.get("quantity")).intValue() : 1;
            service.addItem(username, productId, qty);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateItem(@RequestBody Map<String, Object> req) {
        try {
            String username = (String) req.get("username");
            int productId = ((Number) req.get("productId")).intValue();
            int quantity = ((Number) req.get("quantity")).intValue();

            if (quantity <= 0) {
                service.deleteCompletely(username, productId);
            } else {
                service.setQuantity(username, productId, quantity);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getAllItems(HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("authenticatedUser");
            if (user == null) throw new IllegalArgumentException("Not authenticated");

            List<Cart> carts = service.getAllItems(user.getId());
            List<Map<String, Object>> items = new ArrayList<>();
            BigDecimal grandTotal = BigDecimal.ZERO;

            for (Cart c : carts) {
                Product p = c.getProduct();
                BigDecimal price = p.getPrice();
                int qty = c.getQuantity();
                BigDecimal total = price.multiply(BigDecimal.valueOf(qty));

                Map<String, Object> m = new HashMap<>();
                m.put("productId", p.getProductId());
                m.put("name", p.getName());
                m.put("description", p.getDescription());
                m.put("imageUrl", (p.getImage()!=null)? p.getImage().getImageUrl() : " ");
                m.put("pricePerItem", price);
                m.put("quantity", qty);
                m.put("totalPrice", total);
                items.add(m);
                grandTotal = grandTotal.add(total);
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("username", user.getUsername());
            resp.put("items", items);
            resp.put("grandTotal", grandTotal);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteItem(@RequestBody Map<String, Object> req) {
        try {
            String username = (String) req.get("username");
            Integer productId = req.containsKey("productId")
                    ? ((Number) req.get("productId")).intValue()
                    : null;

            if (productId == null) {
                service.deleteAll(username);
            } else {
                service.deleteCompletely(username, productId);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
package Ecommerce.website.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Ecommerce.website.backend.Entities.User;
import Ecommerce.website.backend.Repos.UsersRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class Auth {

    private final UsersRepo userRepo;
    
    private final BCryptPasswordEncoder passwordEncoder;

//    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
    public Auth(UsersRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register user (encode password before saving)
    public User register(User user) {
        // Encode password before saving to DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // ✅ Authenticate user (compare raw vs encoded)
    public User authenticate(String userName, String password) {
        User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Password is incorrect"));

      
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        return user;
    }
    
    private final String SECRET_KEY = "your-super-long-secret-key-at-least-64-characters-long-abcdefgh1234567890!@#$%^&*()_+";


  
    public String generateToken(User user) {
//        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    	Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    	Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    	Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); 




        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}

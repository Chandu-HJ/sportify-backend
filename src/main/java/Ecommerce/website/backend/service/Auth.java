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
import Ecommerce.website.backend.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class Auth {

    private final AppConfig appConfig;

    private final UsersRepo userRepo;
    
    private final BCryptPasswordEncoder passwordEncoder;

//    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
    public Auth(UsersRepo userRepo, BCryptPasswordEncoder passwordEncoder, AppConfig appConfig) {
        this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.appConfig = appConfig;
    }

   
    public User authenticate(String userName, String password) {
        User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("UserName or Password is incorrect"));

      
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("UserName or Password is incorrect");
        }

        return user;
    }
    
    private final String SECRET_KEY = "your-super-long-secret-key-at-least-64-characters-long-abcdefgh1234567890!@#$%^&*()_+";


  
    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

//    	Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); 




        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +(24* 3600000))) // 1 hour
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            // Build parser with your secret key
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Optional: check expiration
            if (claims.getExpiration().before(new Date())) {
                return false; // token expired
            }

            return true; // token is valid
        } catch (Exception e) {
            // Any exception means token is invalid
            System.out.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }
    
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println(claims.getSubject());
            return claims.getSubject(); // returns username
        } catch (Exception e) {
            return null;
        }
    }


}

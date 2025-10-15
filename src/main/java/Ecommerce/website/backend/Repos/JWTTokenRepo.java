package Ecommerce.website.backend.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ecommerce.website.backend.Entities.JWTToken;

@Repository
public interface JWTTokenRepo extends JpaRepository<JWTToken, Integer> {

}

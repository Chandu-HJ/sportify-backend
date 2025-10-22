package Ecommerce.website.backend.Repos;

import org.springframework.data.jpa.repository.JpaRepository;

import Ecommerce.website.backend.Entities.Orders;

public interface OrdersRepo extends JpaRepository<Orders, String> {

}

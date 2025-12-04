# 🏆 Sportify Backend – Spring Boot E-Commerce API

**Sportify Backend** is an e-commerce backend application built using **Spring Boot**, designed for selling sports items online.  
It provides REST APIs for managing products, users, cart, wishlist, and orders with a secure and scalable architecture.  
This project was created to practice Spring Boot development and learn how real-world e-commerce systems work.

---

## ⭐ Features
- Full e-commerce backend for sports products  
- Product listing, filtering, and search  
- Add to cart / remove from cart  
- Wishlist module  
- Orders & checkout flow  
- User registration & login (JWT optional)  
- Layered architecture (Controller → Service → Repository)  
- MySQL database integration  
- Proper exception handling  

---

## 🚀 Technologies Used
- **Java 17+**  
- **Spring Boot**  
- **Spring Web (REST)**  
- **Spring Data JPA**  
- **MySQL / H2 DB**  
- **Maven**  
- **Lombok** (optional)  

---

## 📁 Project Structure
Sportify-Backend/  
│── src/main/java/...  
│   ├── controller/  
│   ├── service/  
│   ├── repository/  
│   ├── model/  
│   ├── dto/  
│   └── SportifyApplication.java  
│── src/main/resources/  
│   ├── application.properties  
│── pom.xml  
└── README.md  

---

## 🔌 API Endpoints (Examples)

### 🛍️ Products  
- `GET /products` – Get all products  
- `GET /products/{id}` – Get product details  
- `POST /products` – Add product (Admin)  
- `PUT /products/{id}` – Update product  
- `DELETE /products/{id}` – Delete product  

### 👤 Users  
- `POST /auth/register` – Register user  
- `POST /auth/login` – Login  

### 🛒 Cart  
- `POST /cart/add` – Add product to cart  
- `GET /cart/{userId}` – Get user cart  
- `DELETE /cart/remove/{id}` – Remove item  

### ❤️ Wishlist  
- `POST /wishlist/add`  
- `GET /wishlist/{userId}`  

### 📦 Orders  
- `POST /orders/create`  
- `GET /orders/{userId}`  

---

## ⚙️ How to Run
1. Clone the repository  
2. Open in IntelliJ / Eclipse  
3. Set database details in `application.properties`  
4. Run command:  

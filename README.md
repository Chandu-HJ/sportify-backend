# 🏆 Sportify Backend – Spring Boot

**Sportify Backend** is a RESTful sports management API built using **Spring Boot**.  
It provides endpoints to manage players, teams, matches, and user data using a clean layered architecture.  
This project was created to practice backend development, REST API design, and Spring Boot concepts.

---

## ⭐ Features
- RESTful API for sports management  
- Player, Team, and Match modules  
- CRUD operations for all entities  
- Layered architecture (Controller → Service → Repository)  
- Exception handling  
- Uses Spring Data JPA  
- MySQL / H2 database support  
- Cross-Origin support for frontend integration  

---

## 🚀 Technologies Used
- **Java 17+**  
- **Spring Boot**  
- **Spring Web (REST APIs)**  
- **Spring Data JPA**  
- **MySQL / H2**  
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
│   └── SportifyApplication.java  
│── src/main/resources/  
│   ├── application.properties  
│── pom.xml  
└── README.md  

---

## 🔌 API Endpoints (Examples)
### Players  
- `GET /players` – Get all players  
- `POST /players` – Add a new player  
- `PUT /players/{id}` – Update a player  
- `DELETE /players/{id}` – Delete a player  

### Teams  
- `GET /teams`  
- `POST /teams`  

### Matches  
- `GET /matches`  
- `POST /matches`  

---

## ⚙️ How to Run
1. Clone the project  
2. Import into **IntelliJ / Eclipse**  
3. Configure database in `application.properties`  
4. Run using:  

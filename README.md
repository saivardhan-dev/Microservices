# Order Management Application (Spring Boot Microservices) 
### Overview: 
This project is an Order Management System built using Spring Boot Microservices architecture.
The system is divided into two core services:
* Order Service
* Inventory Service
  
This separation ensures clear business boundaries, independent scalability, and better maintainability.

### Architecture
* Client → Order Service → Inventory Service → Database(s)

Each microservice:
* Has its own database
*	Owns its own business logic
*	Communicates via REST APIs 

### Microservices
**Order service:** The Order Service manages the complete lifecycle of an order.

**Inventory Service:** Manages product catalog and stock levels.

### Tech Stack
* Java
* Spring Boot
* Spring Data JPA
* Spring Web (REST APIs)
* No SQL (MongoDB)
* Maven
* Swagger

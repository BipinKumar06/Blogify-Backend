# Blogify-Backend

**Blogify-Backend** is a RESTful blog management API built with **Spring Boot** and **PostgreSQL**, focused on secure authentication, scalable content management, and robust relational data modeling. It exposes well-defined APIs for managing users, blog posts, categories, and tags while adhering to clean architecture principles and industry best practices.

The backend leverages **JWT-based authentication** with **Spring Security** to provide stateless, secure access to protected resources. It supports full CRUD operations for blog entities, including workflows for drafting and publishing posts, and enforces strict authorization rules across all endpoints.

---

## ⚙️ Backend Features

### 🔐 Authentication & Security

* JWT-based authentication and authorization
* Secure login endpoint with token generation
* Protected APIs using Spring Security filters
* Password encryption and role-based access control

### 🌐 REST API Design

* Clean, RESTful endpoints for Users, Posts, Categories, and Tags
* DTO-based request and response handling
* Input validation with consistent API responses

### 🧩 Domain & Data Modeling

* Entity relationships:

    * User ↔ Post (One-to-Many)
    * Category ↔ Post (One-to-Many)
    * Post ↔ Tag (Many-to-Many)
* Proper use of JPA annotations and database constraints

### 🗄️ Persistence Layer

* PostgreSQL with Spring Data JPA
* Repository abstraction for database interactions
* Transaction management with optimized lazy loading

### 🔄 DTO Mapping

* MapStruct for efficient Entity ↔ DTO conversion
* Clear separation between persistence and API models

### 🛡️ Error Handling & Testing

* Centralized exception handling using `@ControllerAdvice`
* Meaningful HTTP status codes and structured error responses
* Repository and controller-level tests

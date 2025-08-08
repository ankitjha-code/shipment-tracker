# Xportel - Backend Internship Task: Shipment Tracking API

This project is a Spring Boot application that provides a RESTful API for tracking shipments, built as a technical assignment for the Backend Internship role at Xportel.

---

## 🚀 Features

* **Create Shipment**: `POST /api/v1/shipments`
* **Fetch Shipment by ID**: `GET /api/v1/shipments/{orderId}`
* **Fetch All Shipments**: `GET /api/v1/shipments`
    * Filter by status: `?status=PENDING`
    * Filter by origin: `?origin=New York`
* **Update Shipment Status**: `PATCH /api/v1/shipments/{orderId}`
    * Enforces a strict lifecycle: `PENDING` → `DISPATCHED` → `IN_TRANSIT` → `DELIVERED`

---

## 🛠️ Tech Stack & Design Choices

* **Java 21 & Spring Boot 3.3.2**: Modern, robust foundation for building REST APIs.
* **Maven**: For dependency management.
* **Layered Architecture**: Clear separation of concerns into Controller, Service, and Repository layers.
* **In-Memory Storage**: Uses a thread-safe `ConcurrentHashMap` for data storage as required.
* **DTO Pattern**: Decouples the API contract from the internal domain model for better stability and security.
* **Centralized Exception Handling**: Provides consistent and clean error responses using `@ControllerAdvice`.
* **Swagger/OpenAPI**: Integrated for live, interactive API documentation.
* **JUnit 5 & Mockito**: For unit testing critical business logic to ensure correctness.

---

## ⚙️ How to Run

1.  **Prerequisites**:
    * Java 17 or newer (Project is configured for Java 21)
    * Maven

2.  **Clone the repository**:
    ```bash
    git clone <your-github-repo-link>
    cd shipment-tracker
    ```

3.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

The application will start on `http://localhost:8080`.

---

## 📄 API Documentation

Once the application is running, the interactive Swagger UI is available at:

[**https://shipment-tracker-1lu7.onrender.com/swagger-ui/index.html**](https://shipment-tracker-1lu7.onrender.com/swagger-ui/index.html)

You can view all endpoints and test the API directly from your browser.
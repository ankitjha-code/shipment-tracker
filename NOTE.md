Design & Logic Explanation for the Shipment Tracking API
Hello,

Thank you for the opportunity. Here is a brief overview of the design choices and logic implemented in the Shipment Tracking API project.

1. Project Structure: Layered Architecture
   I structured the application using a classic 3-layer architecture:

Controller Layer (controller): This is the entry point for all HTTP requests. Its only job is to handle web traffic, validate incoming data, and delegate work to the service layer.

Service Layer (service): This is the brain of the application. It contains all the core business logic, such as the rules for updating a shipment's status. It is completely independent of the web, making it highly testable.

Repository Layer (repository): This layer is responsible for all data access. It abstracts the storage mechanism (in this case, an in-memory ConcurrentHashMap) from the rest of the application.

This separation of concerns makes the code clean, easy to maintain, and simple to test.

2. Core Logic: Status Transition Rules
   The most critical business logic is the shipment status lifecycle (PENDING → DISPATCHED → etc.).

I implemented this rule in the ShipmentServiceImpl using a Map to explicitly define the valid transitions. This approach is declarative, easy to read, and simple to modify if the rules change in the future.

Any invalid transition attempt (e.g., skipping a step or moving backward) throws a custom InvalidStatusTransitionException, ensuring data integrity.

3. API Design and Error Handling
   To create a robust and user-friendly API, I implemented two key patterns:

Data Transfer Objects (DTOs): I used DTOs (CreateShipmentRequest, UpdateStatusRequest) for API requests. This decouples the API contract from the internal data model, which is a crucial best practice for stability and security.

Global Exception Handler (@ControllerAdvice): Instead of handling errors inside the controller, I created a centralized handler. This catches all business-specific exceptions and formats them into a consistent and clean JSON error response for the client.

Finally, I added unit tests for the critical status transition logic and integrated Swagger/OpenAPI to provide live, interactive documentation for the API.

Thank you for your time and consideration.
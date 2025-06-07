# Software Requirements Specification for ShareSphere

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification (SRS) document outlines the requirements for **ShareSphere**, a web-based platform designed to facilitate community resource sharing. Users can list resources (e.g., tools, books, equipment), browse available resources, and request to borrow them. The system addresses the real-world problem of underutilized resources in communities, promoting sustainability and collaboration. The application will be built using Spring Boot, deployed on AWS ECS, and include a simple React frontend to showcase full-stack skills.

### 1.2 Scope
ShareSphere enables users to:
- Register and authenticate securely.
- Create, manage, and share resources.
- Search and filter resources by category, location, or availability.
- Request to borrow resources, with owners approving or rejecting requests.
- Optionally, upload resource images and receive email notifications for request updates.

The system will be a single-service web application with RESTful APIs, a PostgreSQL database, and deployment on AWS ECS using Docker and Fargate. It will include a minimal React frontend for account interaction and leverage AWS services like RDS, S3, and SES.

### 1.3 Definitions, Acronyms, and Abbreviations
- **User**: An individual who registers to browse, list, or borrow resources.
- **Owner**: A account who lists a resource for sharing.
- **Borrower**: A account who requests to borrow a resource.
- **Resource**: An item (e.g., tool, book) listed for sharing.
- **Request**: A borrower’s request to borrow a resource, with statuses (pending, approved, rejected, returned).
- **Spring Boot**: Java framework for building REST APIs.
- **AWS ECS**: Amazon Elastic Container Service for deploying Docker containers.
- **RDS**: Amazon Relational Database Service for hosting PostgreSQL.
- **S3**: Amazon Simple Storage Service for storing resource images.
- **SES**: Amazon Simple Email Service for notifications.
- **JWT**: JSON Web Token for authentication.
- **ALB**: Application Load Balancer for routing traffic.

### 1.4 References
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- AWS ECS Documentation: https://aws.amazon.com/ecs/
- React Documentation: https://react.dev/
- PostgreSQL Documentation: https://www.postgresql.org/docs/

## 2. Overall Description

### 2.1 User Needs
- **Community Members**: Need a platform to share underutilized resources (e.g., tools, books) and borrow from others, reducing costs and waste.
- **Resource Owners**: Require tools to list, manage, and approve/reject borrow requests for their resources.
- **Borrowers**: Need an easy way to search, filter, and request resources, with clear status updates.
- **Administrators (Future Scope)**: May need to moderate listings or resolve disputes (not implemented in this version).

### 2.2 Assumptions and Dependencies
- Users have basic internet access and a web browser or device to access the platform.
- AWS Free Tier is used for cost-effective deployment (ECS, RDS, S3, SES).
- PostgreSQL is available locally or via RDS for development and production.
- Docker is used for containerization, and AWS ECS Fargate is used for deployment.
- Optional React frontend assumes familiarity with JavaScript and npm.

## 3. System Features

### 3.1 User Authentication
**Description**: Users can register, log in, and log out securely. Role-based access distinguishes between owners and borrowers.
- **Input**: Email, password, and optional profile details (e.g., name, location).
- **Output**: JWT token for authenticated sessions, account profile data.
- **Functional Requirements**:
  - Register: Users provide email, password, and location (city/zip code).
  - Login: Users authenticate with email and password to receive a JWT.
  - Logout: Invalidate session/token.
  - Role-Based Access: Owners can manage their resources; borrowers can request resources.
- **Endpoints**:
  - `POST /api/auth/register`: Create a new account.
  - `POST /api/auth/login`: Authenticate and return JWT.
  - `GET /api/auth/profile`: Retrieve account profile (authenticated).

### 3.2 Resource Management
**Description**: Users (owners) can create, update, delete, and list their resources for sharing.
- **Input**: Resource details (name, description, category, availability status, optional image).
- **Output**: List of resources, individual resource details.
- **Functional Requirements**:
  - Create: Owners add a resource with name, description, category (e.g., tools, books), and availability.
  - Update: Owners modify resource details or availability.
  - Delete: Owners remove their resources.
  - List: Owners view their own resources.
- **Endpoints**:
  - `POST /api/resources`: Create a resource (owner only).
  - `PUT /api/resources/{id}`: Update a resource (owner only).
  - `DELETE /api/resources/{id}`: Delete a resource (owner only).
  - `GET /api/resources/mine`: List owner’s resources (authenticated).

### 3.3 Resource Search and Browse
**Description**: Users can browse all available resources and search/filter by category, keyword, or location.
- **Input**: Search query (keyword), filters (category, location, availability).
- **Output**: List of matching resources with details.
- **Functional Requirements**:
  - Browse: Display all available resources with pagination.
  - Search: Filter by keyword (e.g., “drill”), category, or location (city/zip code).
  - Sorting: Sort by name, category, or availability.
- **Endpoints**:
  - `GET /api/resources`: List all available resources (public).
  - `GET /api/search?query={keyword}&category={cat}&location={loc}`: Search with filters.

### 3.4 Borrow Request System
**Description**: Users (borrowers) can request to borrow resources, and owners can approve or reject requests.
- **Input**: Request details (resource ID, borrower ID, borrow dates).
- **Output**: Request status (pending, approved, rejected, returned), request history.
- **Functional Requirements**:
  - Create Request: Borrowers send a request to borrow a resource.
  - Approve/Reject: Owners approve or reject requests.
  - Status Tracking: Both parties view request status and history.
  - Return Confirmation: Owners mark resources as returned.
- **Endpoints**:
  - `POST /api/requests`: Create a borrow request (borrower only).
  - `PUT /api/requests/{id}/approve`: Approve a request (owner only).
  - `PUT /api/requests/{id}/reject`: Reject a request (owner only).
  - `PUT /api/requests/{id}/return`: Mark as returned (owner only).
  - `GET /api/requests/mine`: List account’s requests (authenticated).

### 3.5 Image Upload (Optional)
**Description**: Owners can upload images for resources, stored in AWS S3.
- **Input**: Image file (e.g., PNG, JPEG).
- **Output**: Image URL stored in the resource entity.
- **Functional Requirements**:
  - Upload: Owners upload an image when creating/updating a resource.
  - Retrieve: Images are accessible via S3 URLs.
- **Endpoints**:
  - `POST /api/resources/{id}/image`: Upload image (owner only, integrated with resource creation/update).

### 3.6 Email Notifications (Optional)
**Description**: Send email notifications for request creation, approval, rejection, or return.
- **Input**: Request status changes.
- **Output**: Email sent to relevant users via AWS SES.
- **Functional Requirements**:
  - Notify borrower on request creation, approval, or rejection.
  - Notify owner on new request or return confirmation.
- **Integration**: Use Spring Boot Mail with AWS SES.

### 3.7 Frontend Interface (Optional)
**Description**: A simple React frontend for browsing resources, submitting requests, and managing resources/requests.
- **Input**: User interactions via web interface.
- **Output**: Dynamic UI displaying resources, search results, and request statuses.
- **Functional Requirements**:
  - Display resource catalog with search and filter options.
  - Allow users to log in, register, and view their profile.
  - Enable owners to manage resources and requests.
  - Enable borrowers to submit and track requests.

## 4. External Interface Requirements

### 4.1 User Interfaces
- **REST API**: Exposed via Spring Boot for all backend operations.
- **Frontend (Optional)**: React-based UI with components for:
  - Login/Registration forms.
  - Resource catalog with search/filter.
  - Resource and request management dashboards.
  - Uses Tailwind CSS for styling.

### 4.2 Hardware Interfaces
- None (web-based application).

### 4.3 Software Interfaces
- **Spring Boot**: Version 3.x for REST APIs, JPA, Security, and Mail.
- **PostgreSQL**: Version 15.x for data storage (via AWS RDS).
- **AWS ECS**: Fargate for containerized deployment.
- **AWS Services**:
  - RDS: PostgreSQL database.
  - S3: Image storage (optional).
  - SES: Email notifications (optional).
  - ALB: Traffic routing.
- **Docker**: For containerizing the application.
- **React**: Version 18.x for optional frontend.
- **JWT**: For authentication.
- **Swagger/OpenAPI**: For API documentation.

### 4.4 Communication Interfaces
- **HTTPS**: For secure API communication.
- **SMTP**: For email notifications via AWS SES.
- **S3 API**: For image uploads/downloads.

## 5. Non-Functional Requirements

### 5.1 Performance Requirements
- **Response Time**: API responses under 2 seconds for 95% of requests under normal load.
- **Scalability**: Handle up to 1,000 concurrent users via ECS auto-scaling.
- **Database**: Support up to 10,000 resources and 50,000 requests with optimized queries.

### 5.2 Security Requirements
- **Authentication**: JWT-based, with tokens expiring after 24 hours.
- **Authorization**: Role-based access (owner vs. borrower).
- **Data Protection**: Encrypt sensitive data (e.g., passwords) using bcrypt.
- **AWS Security**: Use IAM roles, VPC, and security groups for ECS and RDS.

### 5.3 Quality Attributes
- **Usability**: Intuitive API and frontend (optional) with clear error messages.
- **Maintainability**: Modular code with separate layers (controllers, services, repositories).
- **Portability**: Containerized with Docker for deployment on any ECS-compatible environment.
- **Reliability**: 99.9% uptime, ensured by ECS and ALB.

### 5.4 Constraints
- Use AWS Free Tier to minimize costs.
- No real-time features (e.g., chat) to keep complexity moderate.
- Single-service architecture to avoid microservices complexity.

## 6. System Architecture

### 6.1 Overview
- **Backend**: Spring Boot application with REST APIs, using MVC architecture.
- **Database**: PostgreSQL hosted on AWS RDS.
- **Frontend (Optional)**: React single-page application (SPA) hosted separately or served via Spring Boot (Thymeleaf alternative).
- **Deployment**: Docker container deployed on AWS ECS with Fargate, using ALB for load balancing.
- **Storage**: AWS S3 for resource images (optional).
- **Notifications**: AWS SES for email (optional).

### 6.2 Data Model
- **User**: `id`, `email`, `password` (hashed), `name`, `location` (city/zip), `role` (OWNER/BORROWER).
- **Resource**: `id`, `name`, `description`, `category`, `availability` (boolean), `owner_id`, `image_url` (optional).
- **Request**: `id`, `resource_id`, `borrower_id`, `status` (PENDING/APPROVED/REJECTED/RETURNED), `borrow_date`, `return_date`.

### 6.3 Deployment Architecture
- **ECS Cluster**: Runs Dockerized Spring Boot app on Fargate.
- **RDS Instance**: PostgreSQL database with secure VPC access.
- **ALB**: Routes HTTPS traffic to ECS tasks.
- **S3 Bucket**: Stores resource images (optional).
- **SES**: Sends email notifications (optional).
- **VPC**: Isolates resources with security groups and subnets.

## 7. Other Requirements

### 7.1 Testing Requirements
- **Unit Tests**: For services (e.g., request approval logic) using JUnit and Mockito.
- **Integration Tests**: For API endpoints using Spring Boot Test.
- **Docker Testing**: Verify container functionality locally with Docker Compose.
- **Load Testing**: Simulate 100 concurrent users to ensure performance.

### 7.2 Documentation Requirements
- **README**: Includes setup, API usage, and ECS deployment instructions.
- **API Documentation**: Generated with Swagger/OpenAPI, accessible at `/swagger-ui`.
- **Postman Collection**: For testing all API endpoints.

### 7.3 Legal Requirements
- Ensure compliance with data protection laws (e.g., GDPR for account data).
- Use AWS services within Free Tier limits to avoid costs.

## 8. Project Milestones
1. **Week 1**: Set up Spring Boot project, define entities, and implement authentication.
2. **Week 2**: Develop resource management and search APIs.
3. **Week 3**: Implement request system and optional image upload (S3).
4. **Week 4**: Add email notifications (SES) and React frontend (optional).
5. **Week 5**: Dockerize the app, deploy to AWS ECS, and test.
6. **Week 6**: Write tests, document the project, and finalize for resume.

## 9. Glossary
- **ShareSphere**: The name of the community resource sharing platform.
- **Fargate**: AWS ECS serverless compute engine.
- **REST API**: Representational State Transfer API for client-server communication.

## 10. Appendix
- **Sample API Request**:
  ```json
  POST /api/resources
  Authorization: Bearer <JWT>
  {
    "name": "Power Drill",
    "description": "Cordless drill, 18V",
    "category": "Tools",
    "availability": true
  }
  ```
- **Sample Response**:
  ```json
  {
    "id": 1,
    "name": "Power Drill",
    "description": "Cordless drill, 18V",
    "category": "Tools",
    "availability": true,
    "ownerId": 123
  }
  ```
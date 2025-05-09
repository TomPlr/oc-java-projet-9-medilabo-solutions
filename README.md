# Medilabo Solutions - Healthcare Management System

## Project Overview
Medilabo Solutions is a comprehensive healthcare management system built using microservices architecture. The system consists of multiple services that work together to provide patient management, medical notes, and health assessment functionalities. This application is designed to help healthcare providers manage patient information, medical records, and health assessments efficiently and securely.

## 📐System Architecture

### Backend Services
1. **Gateway Service (ms-gateway)**
    - Handles API routing and security
    - Implements request validation
    - Manages cross-origin resource sharing (CORS)

2. **Patient Service (ms-patient)**
    - Manages patient information
    - Uses MySQL database for persistent storage
    - Features:
        - Patient personal data management
        - Demographic information management

3. **Notes Service (ms-notes)**
    - Handles medical notes and documentation
    - Uses MongoDB for flexible document storage
    - Features:
        - Real-time note creation and updates
        - Medical observation recording
        - Historical note tracking

4. **Assessment Service (ms-assessment)**
    - Provides health assessment functionality
    - Features:
        - Risk assessment calculations
        - Health status evaluation
        - Medical condition monitoring

5. **Authentication Service (Keycloak)**
    - Custom Keycloak instance deployed on AWS
    - Handles user authentication and authorization
    - Features:
        - Role-based access control
        - User session management
        - Secure token generation and validation

### Frontend
- **UI Service (ms-ui)**
    - Angular-based web interface
    - Features:
        - Responsive design for all devices
        - Real-time data updates
        - User-friendly dashboard
        - Secure authentication flow
        - Interactive patient management interface

## ❗ Prerequisites
- Docker and Docker Compose
    - Docker version 20.10.0 or higher
    - Docker Compose version 2.0.0 or higher
- Java 21 or higher
    - Maven 3.8.0 or higher
- Node.js and npm (for local development)
    - Node.js version 16.x or higher
    - npm version 8.x or higher
- MongoDB Atlas account
    - Free tier or higher
    - Network access configured
- Keycloak server
    - Version 18.0.0 or higher
    - Realm and client configured

## 🧑‍🍳 Environment Setup

### 1. Root Project .env File
Create a `.env` file in the root directory with the following variables:

```env
# MySQL Configuration
MYSQL_ROOT_PASSWORD=your_root_password
PATIENT_DB_HOST=patient-db
PATIENT_DB_PORT=your_db_port
PATIENT_DB_NAME=patientdb
PATIENT_DB_USERNAME=your_db_username
PATIENT_DB_PASSWORD=your_db_password

# MongoDB Configuration
MONGODB_USER=your_mongodb_username
MONGODB_PASSWORD=your_mongodb_password
MONGODB_HOST=your_mongodb_host
MONGODB_DATABASE=your_database_name

# Keycloak Configuration
KEYCLOAK_ISSUER_URI=http://your_keycloak_url/auth/realms/your_realm
KEYCLOAK_JWK_SET_URI=http://your_keycloak_url/auth/realms/your_realm/protocol/openid-connect/certs
```

### 2. UI Service .env File
Create a `.env` file in the `services/ms-ui` directory with the following variables:

```env
# Environment
NG_APP_ENV=dev

# Keycloak Configuration
NG_APP_KEYCLOAK_URL=http://your_keycloak_url:8080/
NG_APP_KEYCLOAK_REALM=your_realm
NG_APP_KEYCLOAK_CLIENT_ID=your_client_id
NG_APP_KEYCLOAK_REDIRECT_URI=your_redirect_uri

# API Configuration
NG_APP_API_GATEWAY_URL=your_gateway_service_url
```

## 🚀 Running the Application

1. **Clone the repository**
   ```bash
   git clone [repository-url]
   cd oc-java-projet-9-medilabo-solutions
   ```

2. **Set up environment files**
    - Create and configure both `.env` files as described above
    - Ensure all sensitive information is properly secured
    - Verify all URLs and endpoints are correctly configured

3. **Build and run with Docker Compose**
   ```bash
   # Build all services
   docker-compose build

   # Start all services
   docker-compose up -d

   # View logs
   docker-compose logs -f
   ```

4. **Access the application**
    - Frontend: http://localhost:4200
    - API Gateway: http://localhost:8080

## 🏗️ Development

### Local Development Setup
1. **Backend Services**
    - Each service can be run independently using Spring Boot
    - Ensure all environment variables are set in your IDE or system
    - Recommended IDE: IntelliJ IDEA or Eclipse with Spring Tools Suite

2. **Frontend Development**
   ```bash
   cd services/ms-ui
   npm install
   npm start
   ```
    - Development server will start at http://localhost:4200
    - Hot reload enabled for development
    - Angular DevTools recommended for debugging

## 👮‍♂️ Authentication and User Roles

The application comes with two preset user profiles for testing and demonstration purposes:

| Role | Username     | Password  | Permissions  |
| :-------- | :------- | :----------| :----------|
| `manager`      | `jdoe` | `test` | Access to patient management - Ability to modify patient personal data - View medical notes and assessments |
| `medical_staff`| `jadoe` | `test` | Access to medical notes - Ability to add and modify medical comments - Influence patient health assessments - View patient information|

### Role-Based Access Control
The application implements strict role-based access control:
- Managers can modify patient personal information but cannot alter medical data
- Medical staff can add medical comments and influence assessments but cannot modify patient personal data
- Both roles can view patient information and medical history

## 🔧 Troubleshooting
Common issues and solutions:
1. **Database Connection Issues**
    - Verify database credentials
    - Check network connectivity
    - Ensure database service is running

2. **Authentication Problems**
    - Verify Keycloak configuration
    - Check JWT token validity
    - Ensure proper role assignments

3. **Service Communication**
    - Check service health endpoints
    - Verify network configuration
    - Review service logs

## ♻️ Green Coding Improvements

### 1. Code Optimization
**Why** - *Efficient code reduces CPU usage and execution time, leading to lower energy consumption.*  
**How** - *Use efficient algorithms and data structures.*  
**Impact** - *<span style="color:#cc3300">Low</span>*

### 2. Minimize Resource Usage
**Why** - *Reducing the use of system resources like memory and disk space can lower energy consumption.*  
**How** - *Optimize data storage by using efficient data types.*  
**Impact** - *<span style="color:#cc3300">Low</span> to <span style="color:#e6ac00">Medium</span>*

### 3. Reduce Network Calls
**Why** - *Network operations consume significant energy, especially on mobile devices.*  
**How** - *Implement caching mechanisms to reduce the number of API calls.*  
**Impact** - *<span style="color:#e6ac00">Medium</span>*

### 4. Server-Side Optimizations
**Why** - *Optimizing server-side code can reduce the energy consumption of data centers.*  
**How** - *Use serverless architectures to scale resources dynamically based on demand.*  
**Impact** - *<span style="color:#71da71">High</span>*

### 5. Renewable Energy Hosting & Hardware Efficiency
**Why** - *Hosting your applications on servers powered by renewable energy and energy-efficient hardware can significantly reduce your carbon footprint.*  
**How** - *Use cloud providers that commit to renewable energy.*  
**Impact** - *<span style="color:#00802b">Very High</span>*

## License

Distributed under MIT License.
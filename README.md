# QA Automation Assignment

This project contains automated API and UI tests for a sample full-stack CRUD application, built with Java, Selenium, and RestAssured.

## 1. Setup Instructions

This project requires a 3-terminal setup to run the application and the tests.

### 1. The Application (Backend + Frontend)

The application being tested is `https://github.com/RameshMF/ReactJS-Spring-Boot-CRUD-Full-Stack-App`.

* **Terminal 1: Run the Backend (API)**
    ```bash
    cd /path/to/ReactJS-Spring-Boot-CRUD-Full-Stack-App/springboot-backend
    mvn spring-boot:run
    ```
  *Server runs on `http://localhost:8080`*

* **Terminal 2: Run the Frontend (UI)**
  *Note: This project requires a Node.js workaround.*
    ```bash
    cd /path/to/ReactJS-Spring-Boot-CRUD-Full-Stack-App/react-frontend
    npm install
    
    # Set this environment variable to fix "ERR_OSSL_EVP_UNSUPPORTED"
    $env:NODE_OPTIONS = "--openssl-legacy-provider"
    
    npm start
    ```
  *Server runs on `http://localhost:3000`*

### 2. The Tests (This Project)

* **Terminal 3: Run the Tests**
  *Once the backend and frontend are running, open a third terminal in this project's root folder.*
    ```bash
    cd /path/to/QA_Test_Assignment
    mvn test
    ```

---

## 2. How to run API and UI tests

All tests can be executed from the command line using Maven:

```bash
mvn test

API Tests: src/test/java/ApiTests.java

UI Tests: src/test/java/UiTests.java

Test reports (JUnit XML/txt) are generated in the /target/surefire-reports directory.

## 3. Tools/libraries used

Java (JDK 21)
Maven (for build and dependency management)
Node.js (npm) (to run the React frontend)
JUnit 5 (for test structure)
RestAssured (for API testing)
Selenium WebDriver (for UI automation)
WebDriverManager (for browser driver setup)

## 4. Any assumptions or notes

Sample Application: The sample app (SafdarJamal/crud-app) and port 5000 in the assignment were contradictory, as the app was frontend-only. To fulfill all requirements, I used a different, full-stack Spring Boot + React application.

Ports: Tests assume the backend API is running on http://localhost:8080 and the frontend UI on http://localhost:3000.

Node.js Version: The React frontend is incompatible with newer Node.js versions (v24+). The $env:NODE_OPTIONS = "--openssl-legacy-provider" command is required to run npm start.

Test Flows: The UiTests file contains 3 key flows: Create, Update, and Delete, as the sample app does not have a "login" flow.
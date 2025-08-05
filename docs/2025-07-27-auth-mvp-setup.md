# AuthService Microservice – MVP Setup (2025-07-27)
**Author:** Jeet Solanki

## Overview

This document outlines the initial setup and architecture of the AuthService microservice for the Brainz project.  
It explains what has been implemented, how the service is organized, current capabilities, and outstanding tasks.

---

## What I Did

### 1. **Project Structure**
- Created a modular Java Spring Boot project with the following packages:
  - `config/` – Security and app configuration
  - `controller/` – REST API endpoints
  - `dto/` – Data Transfer Objects for requests/responses
  - `entity/` – (Reserved for future, not used in database-less auth)
  - `exception/` – (Reserved for future custom exceptions)
  - `repository/` – (Not used in database-less auth)
  - `security/` – (Reserved for future, e.g., filters)
  - `service/` – Business logic (AuthService)
  - `util/` – Utility classes (e.g., JWT utilities)

### 2. **Environment Configuration**
- Split configuration into:
  - `application.properties` (shared, non-sensitive)
  - `application-dev.properties` (MySQL, dev settings)
  - `application-test.properties` (H2, test settings)
- JWT secret is now referenced as an environment variable (`JWT_SECRET`), not hardcoded.

### 3. **DTOs and Auth Logic**
- Created DTOs for login, authentication response, and refresh token.
- AuthService is **database-less**: it delegates user validation to a separate UserService (microservice).
- JWT tokens are generated with user ID as the subject and username as a claim, following industry standards.

### 4. **Security**
- Configured stateless JWT-based authentication.
- No sensitive data is committed to the repo.
- `.dockerignore` and `.gitignore` are set up to exclude dev and secret files.

### 5. **CI/CD**
- GitHub Actions workflow runs tests and builds Docker images.
- CI uses the `test` profile, ensuring safe, isolated builds.

---

## What We Can Do Now

- Accept login and refresh token requests via REST API.
- Generate and validate JWT tokens with user ID as the subject.
- Communicate with a UserService microservice for user validation.
- Run automated tests and builds in CI/CD with safe, environment-specific config.
- Easily extend the service for new features (e.g., logout, token blacklist, etc.).

---

## What’s Needed for Production

- Set the `JWT_SECRET` environment variable securely in production.
- Implement the UserService microservice for user registration, validation, and profile management.
- Add more tests (unit, integration, security).
- Add API documentation (e.g., Swagger/OpenAPI).
- Add exception handling and error responses.
- Add monitoring/health checks (Spring Boot Actuator).
- (Optional) Add rate limiting, logging, and audit features.

---

## What Remains / Next Steps

- Write test cases
- Implement UserService and connect it to a real database.
- Add registration, password reset, and user profile endpoints.
- Harden security (token blacklist, refresh token rotation, etc.).
- Write more documentation for each new feature or milestone.
- Regularly update the `docs/` folder with new design decisions and features.

---

## How to Use This Documentation

- Keep this file in `docs/` as a permanent record of the MVP setup.
- For each new feature or milestone, add a new markdown file in `docs/` (e.g., `2025-07-27-registration-feature.md`).
- Copy/paste or link these docs to your knowledge base repo as needed.

---

**Last updated:** none
**Author:** xyz 
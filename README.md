# 🧠 Brainz - Authentication Service

A modular authentication microservice built with Spring Boot to support all Brainz platform features like JWT, OAuth2, and OTP via Email, SMS, WhatsApp.

---

## ✨ Features

- ✅ JWT-based login and token refresh
- 🔐 OAuth2 (Google, GitHub) login
- 🔁 OTP login (Email, SMS, WhatsApp)
- 📬 Email via SMTP (configurable)
- 🧪 Ready for integration testing
- 🐳 Dockerized with GitHub Actions CI/CD

---

## 🗂 Project Structure

```txt
src/main/java/com/brainz/authservice  
├── config  
├── controller  
├── dto  
├── entity  
├── repository  
├── security  
├── service  
└── utils
resources/  
└── application.yml  
```

```txt
# Clone the repository
git clone https://github.com/brainz-org/auth-service.git
cd auth-service

# Run with Maven
./mvnw spring-boot:run

# Build Docker image
docker build -t brainz-auth-service .

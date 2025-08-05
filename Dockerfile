# Dockerfile Flow and Notes

# ----------------------------------------
# Stage 1: Build the Spring Boot Application
# ----------------------------------------
# 1. Use Eclipse Temurin JDK 17 as the build image (provides Java compiler and tools)
FROM eclipse-temurin:17-jdk AS build

# 2. Set the working directory inside the container to /app
WORKDIR /app

# 3. Copy all project files from the host into the container's /app directory
COPY . .

# 4. Ensure the Maven wrapper script (mvnw) is executable
RUN chmod +x ./mvnw

# 5. Build the application using Maven, skipping tests for faster build
RUN ./mvnw clean package -DskipTests

# ----------------------------------------
# Stage 2: Create a Lightweight Runtime Image
# ----------------------------------------
# 6. Use Eclipse Temurin JRE 17 as the runtime image (smaller, no compiler)
FROM eclipse-temurin:17-jre

# 7. Set the working directory inside the runtime container to /app
WORKDIR /app

# 8. Copy the built JAR file from the build stage into the runtime image
COPY --from=build /app/target/*.jar app.jar

# 9. Expose port 8080 (the default for Spring Boot apps)
EXPOSE 8080

# 10. Set the default command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]

# ----------------------------------------
# Notes:
# - This Dockerfile uses multi-stage builds to keep the final image small and secure.
# - The build stage compiles the code and packages it as a JAR.
# - The runtime stage only contains the JRE and the packaged JAR, reducing attack surface and image size.
# - The application will be accessible on port 8080.
# - The Maven wrapper (mvnw) ensures consistent Maven version usage across environments.




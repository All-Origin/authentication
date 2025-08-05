package com.Junior.authentication;

// Importing necessary Spring Boot classes
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The @SpringBootApplication annotation is a convenience annotation that adds:
 *   - @Configuration: Tags the class as a source of bean definitions for the application context.
 *   - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 *   - @ComponentScan: Tells Spring to look for other components, configurations, and services in the com.brainz.authentication package, allowing it to find controllers and other classes.
 *
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class AuthenticationApplication {

    /**
     * The main() method is the starting point of any Java application.
     * SpringApplication.run() bootstraps the application, starting the Spring context and the embedded server (like Tomcat).
     *
     * @param args Command-line arguments passed when starting the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

}

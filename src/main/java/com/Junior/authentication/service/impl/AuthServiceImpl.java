package com.Junior.authentication.service.impl;

import com.Junior.authentication.dto.AuthResponseDto;
import com.Junior.authentication.dto.LoginRequestDto;
import com.Junior.authentication.dto.RefreshTokenRequestDto;
import com.Junior.authentication.dto.UserValidationResponse;
import com.Junior.authentication.enums.Roles;
import com.Junior.authentication.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.Junior.authentication.service.AuthService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        try {
            // Call UserService to validate user credentials
            // Create the body object (e.g., a DTO)
            LoginRequestDto requestBody = new LoginRequestDto();
            requestBody.setUsername(request.getUsername());
            requestBody.setPassword(request.getPassword()); // example

// Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

// Combine headers and body
            HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(requestBody, headers);

// Make POST call
            ResponseEntity<UserValidationResponse> response = restTemplate.postForEntity(
                    userServiceUrl + "/api/user/validate",
                    requestEntity,
                    UserValidationResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                UserValidationResponse user = response.getBody();

                // Generate JWT tokens with user ID as subject
                String accessToken = jwtUtil.generateToken(user.getUserId(), user.getUserName(),user.getRoles());
                String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), user.getUserName(),user.getRoles());

                return new AuthResponseDto(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    86400000L, // 24 hours
                    user.getUserName(),
                    user.getEmail(),
                "Login successful"
                );
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        try {
            // Validate refresh token
            Long userId = jwtUtil.extractUserId(request.getRefreshToken());
            String username = jwtUtil.extractUsername(request.getRefreshToken());
            List<Roles> roles = jwtUtil.extractRoles(request.getRefreshToken());
            if (jwtUtil.validateToken(request.getRefreshToken(), userId.toString())) {

                // Generate new tokens
                String newAccessToken = jwtUtil.generateToken(userId, username,roles);
                String newRefreshToken = jwtUtil.generateRefreshToken(userId, username,roles);

                return new AuthResponseDto(
                    newAccessToken,
                    newRefreshToken,
                    "Bearer",
                    86400000L,
                    username,
                    null, // Email not needed for refresh
                    "Token refreshed successfully"
                );
            } else {
                throw new RuntimeException("Invalid refresh token");
            }
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new RuntimeException("Token refresh failed", e);
        }
    }

    @Override
    public void logout(String token) {
        // In a real implementation, you might want to blacklist the token
        // For now, we'll just log the logout
        String username = jwtUtil.extractUsername(token);
        log.info("User {} logged out", username);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Long userId = jwtUtil.extractUserId(token);
            return jwtUtil.validateToken(token, userId.toString());
        } catch (Exception e) {
            return false;
        }
    }

    // Inner class for UserService response

} 
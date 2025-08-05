package com.Junior.authentication.service;

import com.Junior.authentication.dto.AuthResponseDto;
import com.Junior.authentication.dto.LoginRequestDto;
import com.Junior.authentication.dto.RefreshTokenRequestDto;

public interface AuthService {
    
    /**
     * Authenticate user and generate JWT tokens
     */
    AuthResponseDto login(LoginRequestDto request);
    
    /**
     * Refresh access token using refresh token
     */
    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
    
    /**
     * Logout user and invalidate tokens
     */
    void logout(String token);
    
    /**
     * Validate JWT token
     */
    boolean validateToken(String token);
}
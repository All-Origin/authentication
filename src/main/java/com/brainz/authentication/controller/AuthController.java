package com.brainz.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainz.authentication.dto.LoginRequestDto;
import com.brainz.authentication.dto.AuthResponseDto;
import com.brainz.authentication.dto.RefreshTokenRequestDto;
import com.brainz.authentication.service.AuthService;

/**
 * AuthController handles authentication-related HTTP requests such as user login and token refresh.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param loginRequestDto the login request containing user credentials
     * @return a ResponseEntity containing the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes the authentication token using the provided refresh token request.
     *
     * @param refreshTokenRequestDto the request containing refresh token information
     * @return a ResponseEntity containing the new authentication tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        AuthResponseDto response = authService.refreshToken(refreshTokenRequestDto);
        return ResponseEntity.ok(response);
    }
}
package com.openclassrooms.chatop.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.dto.TokenResponse;
import com.openclassrooms.chatop.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
		return new TokenResponse(authService.register(request));
	}

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest request) {
		return new TokenResponse(authService.login(request));
	}

}

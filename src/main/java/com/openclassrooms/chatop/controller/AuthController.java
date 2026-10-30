package com.openclassrooms.chatop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.dto.TokenResponse;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.AuthService;
import com.openclassrooms.chatop.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@PostMapping("/register")
	public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
		return new TokenResponse(authService.register(request));
	}

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest request) {
		return new TokenResponse(authService.login(request));
	}

	/**
	 * The authenticated user is the subject (email) of the JWT sent in the Authorization header.
	 */
	@GetMapping("/me")
	public UserResponse me(Authentication authentication) {
		return userService.getByEmail(authentication.getName());
	}

}

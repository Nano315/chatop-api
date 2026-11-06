package com.openclassrooms.chatop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.dto.ErrorResponse;
import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.dto.TokenResponse;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.AuthService;
import com.openclassrooms.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Account creation, login and current user")
public class AuthController {

	private final AuthService authService;
	private final UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	/**
	 * Creates an account and returns a JWT for it.
	 */
	@PostMapping("/register")
	@SecurityRequirements
	@Operation(summary = "Create an account", description = "Public route. Returns a JWT for the new user.")
	@ApiResponse(responseCode = "200", description = "Account created")
	@ApiResponse(responseCode = "400", description = "Missing or invalid field, or email already used",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
		return new TokenResponse(authService.register(request));
	}

	/**
	 * Returns a JWT if the email and password match an existing account.
	 */
	@PostMapping("/login")
	@SecurityRequirements
	@Operation(summary = "Log in", description = "Public route. Returns a JWT if the email and password are correct.")
	@ApiResponse(responseCode = "200", description = "Logged in")
	@ApiResponse(responseCode = "401", description = "Wrong email or password",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public TokenResponse login(@Valid @RequestBody LoginRequest request) {
		return new TokenResponse(authService.login(request));
	}

	/**
	 * Returns the authenticated user, i.e. the subject (email) of the JWT sent in the Authorization header.
	 */
	@GetMapping("/me")
	@Operation(summary = "Get the authenticated user")
	@ApiResponse(responseCode = "200", description = "Authenticated user")
	@ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content)
	public UserResponse me(Authentication authentication) {
		return userService.getByEmail(authentication.getName());
	}

}

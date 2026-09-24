package com.gab.nutri_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gab.nutri_api.dto.auth.AuthResponse;
import com.gab.nutri_api.dto.auth.LoginRequest;
import com.gab.nutri_api.dto.auth.RegisterRequest;
import com.gab.nutri_api.dto.auth.UserResponse;
import com.gab.nutri_api.service.auth.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	@PostMapping("/inscription")
	public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
		authService.inscriptionUtilisateur(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/connexion")
	public AuthResponse login(@RequestBody LoginRequest request) {
		return authService.connexionUtilisateur(request);
	}

	@GetMapping("/me")
	public UserResponse getUser(Authentication authentication) {
		return authService.getUtilisateur(authentication.getName());
	}

}

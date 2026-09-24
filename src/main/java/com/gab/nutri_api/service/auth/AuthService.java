package com.gab.nutri_api.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.auth.AuthResponse;
import com.gab.nutri_api.dto.auth.LoginRequest;
import com.gab.nutri_api.dto.auth.RegisterRequest;
import com.gab.nutri_api.dto.auth.UserResponse;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.model.Utilisateur;
import com.gab.nutri_api.model.enums.RoleUtilisateur;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;
import com.gab.nutri_api.repository.UtilisateurRepository;
import com.gab.nutri_api.security.JwtService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthService {

	private final UtilisateurRepository utilisateurRepository;
	private final PatientRepository patientRepository;
	private final DieteticienRepository dieteticienRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(UtilisateurRepository utilisateurRepository, PatientRepository patientRepository,
			DieteticienRepository dieteticienRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {
		super();
		this.utilisateurRepository = utilisateurRepository;
		this.patientRepository = patientRepository;
		this.dieteticienRepository = dieteticienRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	// Inscription utilisateur
	public void inscriptionUtilisateur(RegisterRequest registerRequest) {

		Utilisateur utilisateur = new Utilisateur();

		utilisateur.setEmail(registerRequest.getEmail());
		utilisateur.setNom(registerRequest.getNom());
		utilisateur.setPrenom(registerRequest.getPrenom());
		utilisateur.setRole(registerRequest.getRole());

		// Encodage du mdp
		String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
		utilisateur.setPassword(hashedPassword);

		utilisateurRepository.save(utilisateur);

		// Création patient ou diet
		if (registerRequest.getRole() == RoleUtilisateur.DIETETICIEN) {
			Dieteticien dieteticien = new Dieteticien();
			dieteticien.setRpps(registerRequest.getRpps());
			dieteticien.setUtilisateur(utilisateur);

			dieteticienRepository.save(dieteticien);

		} else if (registerRequest.getRole() == RoleUtilisateur.PATIENT) {
			Patient patient = new Patient();
			patient.setUtilisateur(utilisateur);
			patient.setTaille(registerRequest.getTaille());
			patient.setPoids(registerRequest.getPoids());
			patient.setNap(registerRequest.getNap());
			patient.setGenre(registerRequest.getGenre());
			patient.setDate(registerRequest.getDateDeNaissance());

			if (registerRequest.getDieteticienId() != null) {
				Dieteticien diet = dieteticienRepository.findById(registerRequest.getDieteticienId())
						.orElseThrow(() -> new EntityNotFoundException("Diététicien introuvable"));

				patient.setDieteticien(diet);
			}

			patientRepository.save(patient);
		}
	}

	// Connexion utilisateur
	public AuthResponse connexionUtilisateur(LoginRequest loginRequest) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		Utilisateur utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		String token = jwtService.generateToken(utilisateur);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(token);

		return authResponse;
	}

	public UserResponse getUtilisateur(String email) {

		UserResponse userResponse = new UserResponse();

		Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

		userResponse.setEmail(utilisateur.getEmail());
		userResponse.setId(utilisateur.getId());
		userResponse.setNom(utilisateur.getNom());
		userResponse.setPrenom(utilisateur.getPrenom());
		userResponse.setRole(utilisateur.getRole());

		return userResponse;
	}
}

package com.gab.nutri_api.service.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gab.nutri_api.dto.auth.RegisterRequest;
import com.gab.nutri_api.model.enums.RoleUtilisateur;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;
import com.gab.nutri_api.repository.UtilisateurRepository;
import com.gab.nutri_api.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private UtilisateurRepository utilisateurRepository;

	@Mock
	private PatientRepository patientRepository;

	@Mock
	private DieteticienRepository dieteticienRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private AuthService authService;
	
	@Test
	void inscriptionUtilisateur_patientCreeUtilisateurEtPatient() {

		RegisterRequest registerRequest = new RegisterRequest();

		registerRequest.setEmail("patient@test.fr");
		registerRequest.setPassword("password");
		registerRequest.setRole(RoleUtilisateur.PATIENT);

		when(passwordEncoder.encode("password")).thenReturn("hashed-password");

		authService.inscriptionUtilisateur(registerRequest);

		verify(utilisateurRepository).save(argThat(utilisateur -> utilisateur.getEmail().equals("patient@test.fr")
				&& utilisateur.getRole() == RoleUtilisateur.PATIENT
				&& utilisateur.getPassword().equals("hashed-password")));

		verify(patientRepository).save(argThat(patient -> patient.getUtilisateur() != null
				&& patient.getUtilisateur().getEmail().equals("patient@test.fr")));
	}
	
	@Test
	void inscriptionUtilisateur_dieteticienCreeUtilisateurEtDieteticien() {

		RegisterRequest registerRequest = new RegisterRequest();

		registerRequest.setEmail("diet@test.fr");
		registerRequest.setPassword("password");
		registerRequest.setRpps("1234");
		registerRequest.setRole(RoleUtilisateur.DIETETICIEN);

		when(passwordEncoder.encode("password")).thenReturn("hashed-password");

		authService.inscriptionUtilisateur(registerRequest);

		verify(utilisateurRepository).save(argThat(utilisateur -> utilisateur.getEmail().equals("diet@test.fr")
				&& utilisateur.getRole() == RoleUtilisateur.DIETETICIEN
				&& utilisateur.getPassword().equals("hashed-password")));

		verify(dieteticienRepository).save(argThat(dieteticien -> dieteticien.getUtilisateur() != null
				&& dieteticien.getUtilisateur().getEmail().equals("diet@test.fr") && dieteticien.getRpps().equals("1234")));
	}

	@Test
	void inscriptionUtilisateur_adminEstRefuse() {

		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("admin@test.fr");
		registerRequest.setPassword("password");
		registerRequest.setRole(RoleUtilisateur.ADMIN);

		assertThrows(IllegalArgumentException.class, () -> authService.inscriptionUtilisateur(registerRequest));

		verifyNoInteractions(utilisateurRepository, patientRepository, dieteticienRepository, passwordEncoder);
	}

}

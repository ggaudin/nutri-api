package com.gab.nutri_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Utilisateur;
import com.gab.nutri_api.model.enums.RoleUtilisateur;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;
import com.gab.nutri_api.repository.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTest {

	@Mock
	private PatientRepository patientRepository;

	@Mock
	private DieteticienRepository dieteticienRepository;

	@Mock
	private UtilisateurRepository utilisateurRepository;

	@InjectMocks
	private AccessService accessService;

	@Test
	void patientPeutAccederASonProprePlan() {

		String email = "patient@test.fr";

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail(email);
		utilisateur.setRole(RoleUtilisateur.PATIENT);

		Patient patient = new Patient();
		patient.setId(1);

		PlanAlimentaire plan = new PlanAlimentaire();
		plan.setPatient(patient);

		when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));

		when(patientRepository.findByUtilisateurEmail(email)).thenReturn(Optional.of(patient));

		assertDoesNotThrow(() -> accessService.verificationsAccesPlanAlimentaire(plan, email));
	}

	@Test
	void patientNePeutPasAccederAuPlanDUnAutrePatient() {

		String email = "patient1@test.fr";

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail(email);
		utilisateur.setRole(RoleUtilisateur.PATIENT);

		Patient patientConnecte = new Patient();
		patientConnecte.setId(1);

		Patient autrePatient = new Patient();
		autrePatient.setId(2);

		PlanAlimentaire plan = new PlanAlimentaire();
		plan.setPatient(autrePatient);

		when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));

		when(patientRepository.findByUtilisateurEmail(email)).thenReturn(Optional.of(patientConnecte));

		assertThrows(org.springframework.security.access.AccessDeniedException.class,
				() -> accessService.verificationsAccesPlanAlimentaire(plan, email));
	}
	
}

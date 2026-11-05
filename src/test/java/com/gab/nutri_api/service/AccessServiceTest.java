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

import org.springframework.security.access.AccessDeniedException;

import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Utilisateur;
import com.gab.nutri_api.model.enums.RoleUtilisateur;
import com.gab.nutri_api.model.enums.TypePlan;
import com.gab.nutri_api.model.enums.VisibilitePlan;
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

	private PlanAlimentaire templateGlobal() {
		PlanAlimentaire plan = new PlanAlimentaire();
		plan.setType(TypePlan.TEMPLATE);
		plan.setVisibilite(VisibilitePlan.GLOBAL);
		return plan;
	}

	private Dieteticien dietConnecte(String email) {
		Dieteticien diet = new Dieteticien();
		diet.setId(1);
		when(dieteticienRepository.findByUtilisateurEmail(email)).thenReturn(Optional.of(diet));
		return diet;
	}

	@Test
	void dietPeutConsulterUnTemplateGlobal() {

		String email = "diet@test.fr";
		dietConnecte(email);

		assertDoesNotThrow(() -> accessService.verificationAccesPlanParDiet(email, templateGlobal()));
	}

	@Test
	void dietNePeutPasModifierOuSupprimerUnTemplateGlobal() {

		String email = "diet@test.fr";
		dietConnecte(email);

		assertThrows(AccessDeniedException.class,
				() -> accessService.verificationModificationPlanParDiet(email, templateGlobal()));
	}

	@Test
	void dietPeutModifierOuSupprimerSonPropreTemplate() {

		String email = "diet@test.fr";
		Dieteticien diet = dietConnecte(email);

		PlanAlimentaire plan = new PlanAlimentaire();
		plan.setType(TypePlan.TEMPLATE);
		plan.setDieteticien(diet);

		assertDoesNotThrow(() -> accessService.verificationModificationPlanParDiet(email, plan));
	}

	// Changement de diet : le patient passe du diet A (id 1) au diet B (id 2)

	private PlanAlimentaire planPatient(Dieteticien auteur, Dieteticien dietDuPatient) {
		Patient patient = new Patient();
		patient.setId(10);
		patient.setDieteticien(dietDuPatient);

		PlanAlimentaire plan = new PlanAlimentaire();
		plan.setPatient(patient);
		plan.setDieteticien(auteur);
		return plan;
	}

	private Dieteticien autreDiet() {
		Dieteticien diet = new Dieteticien();
		diet.setId(2);
		return diet;
	}

	@Test
	void dietAccedeAuPlanQuIlACreePourUnPatientQuIlSuit() {

		String email = "dietA@test.fr";
		Dieteticien dietA = dietConnecte(email);

		PlanAlimentaire plan = planPatient(dietA, dietA);

		assertDoesNotThrow(() -> accessService.verificationAccesPlanParDiet(email, plan));
		assertDoesNotThrow(() -> accessService.verificationModificationPlanParDiet(email, plan));
	}

	@Test
	void ancienDietNAccedePlusAuxPlansDUnPatientQuIlNeSuitPlus() {

		String email = "dietA@test.fr";
		Dieteticien dietA = dietConnecte(email);

		PlanAlimentaire plan = planPatient(dietA, autreDiet());

		assertThrows(AccessDeniedException.class, () -> accessService.verificationAccesPlanParDiet(email, plan));
		assertThrows(AccessDeniedException.class,
				() -> accessService.verificationModificationPlanParDiet(email, plan));
	}

	@Test
	void nouveauDietNAccedePasAuxPlansDuDietPrecedent() {

		String email = "dietB@test.fr";
		Dieteticien dietB = dietConnecte(email);

		PlanAlimentaire plan = planPatient(autreDiet(), dietB);

		assertThrows(AccessDeniedException.class, () -> accessService.verificationAccesPlanParDiet(email, plan));
	}

	@Test
	void dietNAccedePlusAuxPlansDUnPatientSansDiet() {

		String email = "dietA@test.fr";
		Dieteticien dietA = dietConnecte(email);

		PlanAlimentaire plan = planPatient(dietA, null);

		assertThrows(AccessDeniedException.class, () -> accessService.verificationAccesPlanParDiet(email, plan));
	}

	@Test
	void patientLitTousSesPlansQuelQueSoitLAuteur() {

		String email = "patient@test.fr";

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail(email);
		utilisateur.setRole(RoleUtilisateur.PATIENT);

		Dieteticien ancienDiet = new Dieteticien();
		ancienDiet.setId(1);

		// plan créé par l'ancien diet A, le patient est désormais suivi par B
		PlanAlimentaire plan = planPatient(ancienDiet, autreDiet());

		when(utilisateurRepository.findByEmail(email)).thenReturn(Optional.of(utilisateur));
		when(patientRepository.findByUtilisateurEmail(email)).thenReturn(Optional.of(plan.getPatient()));

		assertDoesNotThrow(() -> accessService.verificationsAccesPlanAlimentaire(plan, email));
	}

}

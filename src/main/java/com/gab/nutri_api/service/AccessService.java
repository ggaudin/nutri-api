package com.gab.nutri_api.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.PatientDieteticien;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Utilisateur;
import com.gab.nutri_api.model.enums.RoleUtilisateur;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;
import com.gab.nutri_api.repository.UtilisateurRepository;

@Service
public class AccessService {

	private final PatientRepository patientRepository;
	private final DieteticienRepository dieteticienRepository;
	private final UtilisateurRepository utilisateurRepository;
	
	public AccessService(PatientRepository patientRepository,
			DieteticienRepository dieteticienRepository, UtilisateurRepository utilisateurRepository) {
		super();
		this.patientRepository = patientRepository;
		this.dieteticienRepository = dieteticienRepository;
		this.utilisateurRepository = utilisateurRepository;
	}

	// Récupère le patient et le diétiéticien connecté en base
	// Vérifie que le Diet connecté est bien le diet du patient
	public PatientDieteticien recuperationPatientDieteticienEtVerificationAcces(Integer patientId, String dietEmail) {

		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		Dieteticien dietConnecte = dieteticienRepository.findByUtilisateurEmail(dietEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		if (patient.getDieteticien() == null || !patient.getDieteticien().getId().equals(dietConnecte.getId())) {
			throw new AccessDeniedException("Vous n'avez pas accès à ce patient");
		}

		return new PatientDieteticien(patient, dietConnecte);

	}

	public void verificationsAccesPlanAlimentaire(PlanAlimentaire planAlimentaire, String utilisateurEmail) {

		Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Utilisateur introuvable pour l'email :" + utilisateurEmail));

		if (utilisateur.getRole() == RoleUtilisateur.PATIENT) {
			Patient patient = patientRepository.findByUtilisateurEmail(utilisateurEmail).orElseThrow(
					() -> new UsernameNotFoundException("Patient introuvable pour l'email :" + utilisateurEmail));

			if (planAlimentaire.getPatient() == null || !planAlimentaire.getPatient().getId().equals(patient.getId())) {
				throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
			}

		} else if (utilisateur.getRole() == RoleUtilisateur.DIETETICIEN) {

			verificationAccesPlanParDiet(utilisateurEmail, planAlimentaire);

		} else { // Si c'est un admin (n'a pas accès aux données de santé des patients)

			if (planAlimentaire.getDieteticien() != null || planAlimentaire.getPatient() != null) {
				throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
			}

		}
	}

	public void verificationRole(String utilisateurEmail) {

		Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Utilisateur introuvable pour l'email :" + utilisateurEmail));

		if (utilisateur.getRole() == RoleUtilisateur.PATIENT || utilisateur.getRole() == RoleUtilisateur.ADMIN) {
			throw new AccessDeniedException("Vous ne pouvez pas modifier ou supprimer ce plan alimentaire");
		}

	}

	public void verificationAccesPlanParDiet(String utilisateurEmail, PlanAlimentaire planAlimentaire) {

		Dieteticien diet = dieteticienRepository.findByUtilisateurEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Dieteticien introuvable pour l'email :" + utilisateurEmail));

		// Si le plan appartient à un patient sans diet -> Accès refusé
		if (planAlimentaire.getDieteticien() == null && planAlimentaire.getPatient() != null) {
			throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
		}
		// Si le plan appartient à un autre diététicien -> Accès refusé
		if (planAlimentaire.getDieteticien() != null
				&& !planAlimentaire.getDieteticien().getId().equals(diet.getId())) {
			throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
		}
	}

}

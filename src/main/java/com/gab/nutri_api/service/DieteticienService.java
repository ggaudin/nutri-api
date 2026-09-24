package com.gab.nutri_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.DieteticienResponse;
import com.gab.nutri_api.dto.PatientDieteticien;
import com.gab.nutri_api.dto.PatientListResponse;
import com.gab.nutri_api.dto.PatientResponse;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;

@Service
public class DieteticienService {

	private final PatientRepository patientRepository;
	private final DieteticienRepository dieteticienRepository;
	private final AccessService planAlimentaireAccessService;
	
	public DieteticienService(PatientRepository patientRepository, DieteticienRepository dieteticienRepository,
			AccessService planAlimentaireAccessService) {
		super();
		this.patientRepository = patientRepository;
		this.dieteticienRepository = dieteticienRepository;
		this.planAlimentaireAccessService = planAlimentaireAccessService;
	}

	// Retourne les données du profil d'un diet identifié par son email.
	public DieteticienResponse getProfil(String email) {
		DieteticienResponse dieteticienResponse = new DieteticienResponse();

		Dieteticien diet = dieteticienRepository.findByUtilisateurEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		dieteticienResponse.setId(diet.getId());
		dieteticienResponse.setEmail(diet.getUtilisateur().getEmail());
		dieteticienResponse.setNom(diet.getUtilisateur().getNom());
		dieteticienResponse.setPrenom(diet.getUtilisateur().getPrenom());
		dieteticienResponse.setRpps(diet.getRpps());

		return dieteticienResponse;
	}

	// Retourne la liste des patients associés à un diététicien identifié par son
	// email.
	public List<PatientListResponse> getPatients(String email) {

		Dieteticien diet = dieteticienRepository.findByUtilisateurEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		List<PatientListResponse> patients = new ArrayList<PatientListResponse>();

		List<Patient> listePatients = patientRepository.findPatientByDieteticienId(diet.getId());

		for (Patient patient : listePatients) {

			PatientListResponse patientListResponse = new PatientListResponse();
			patientListResponse.setId(patient.getId());
			patientListResponse.setNom(patient.getUtilisateur().getNom());
			patientListResponse.setPrenom(patient.getUtilisateur().getPrenom());

			patients.add(patientListResponse);

		}

		return patients;
	}

	// Retourne les infos d'un patient à partir de son id, à condition que le
	// diététicien connecté soit bien le diététicien du patient
	public PatientResponse getPatient(Integer idPatient, String dietEmail) {

		PatientDieteticien patientDieteticien = planAlimentaireAccessService
				.recuperationPatientDieteticienEtVerificationAcces(idPatient, dietEmail);

		Patient patient = patientDieteticien.patient();

		PatientResponse patientResponse = new PatientResponse();

		patientResponse.setId(patient.getId());
		patientResponse.setEmail(patient.getUtilisateur().getEmail());
		patientResponse.setNom(patient.getUtilisateur().getNom());
		patientResponse.setPrenom(patient.getUtilisateur().getPrenom());
		patientResponse.setDateNaissance(patient.getDate());
		patientResponse.setGenre(patient.getGenre());
		patientResponse.setNap(patient.getNap());
		patientResponse.setPoids(patient.getPoids());
		patientResponse.setTaille(patient.getTaille());

		return patientResponse;
	}

	public Dieteticien getDieteticien(String dieteticienEmail) {

		return dieteticienRepository.findByUtilisateurEmail(dieteticienEmail).orElseThrow(
				() -> new UsernameNotFoundException("Dieteticien introuvable pour l'email :" + dieteticienEmail));

	}

}

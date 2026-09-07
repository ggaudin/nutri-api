package com.gab.nutri_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.DieteticienResponse;
import com.gab.nutri_api.dto.PatientListResponse;
import com.gab.nutri_api.dto.PatientResponse;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;
import com.gab.nutri_api.repository.UtilisateurRepository;

@Service
public class DieteticienService {
	
	@Autowired
	UtilisateurRepository utilisateurRepo;
	
	@Autowired
	private PatientRepository patientRepo;
	
	@Autowired
	private DieteticienRepository dieteticienRepo;
	
	
	//Retourne les données du profil d'un diet identifié par son email.
	public DieteticienResponse getProfil(String email) {
		DieteticienResponse dietDTO = new DieteticienResponse();
		
		Dieteticien diet = dieteticienRepo.findByUtilisateurEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
		
		dietDTO.setId(diet.getId());
		dietDTO.setEmail(diet.getUtilisateur().getEmail());
		dietDTO.setNom(diet.getUtilisateur().getNom());
		dietDTO.setPrenom(diet.getUtilisateur().getPrenom());
		dietDTO.setRpps(diet.getRpps());
		
		return dietDTO;
	}
	
	
	//Retourne la liste des patients associés à un diététicien identifié par son email.
	public List<PatientListResponse> getPatients(String email) {
		
		Dieteticien diet = dieteticienRepo.findByUtilisateurEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
		
		Integer idDiet = diet.getId();
		
		List<PatientListResponse> patients = new ArrayList<PatientListResponse>();
		
		List<Patient> listePatients = patientRepo.findPatientByDieteticienId(idDiet);
		
		for(Patient patient : listePatients) {
			
			PatientListResponse patientLight = new PatientListResponse();
			patientLight.setId(patient.getId());
			patientLight.setNom(patient.getUtilisateur().getNom());
			patientLight.setPrenom(patient.getUtilisateur().getPrenom());
			
			patients.add(patientLight);
			
		}
		
		return patients;
	}
	
	//Retourne les infos d'un patient à partir de son id, à condition que le diététicien connecté soit bien le diététicien du patient
	public PatientResponse getPatient(Integer idPatient, String emailDiet) {
				
		Patient patient = this.recuperationPatientEtVerificationAcces(idPatient, emailDiet);
				
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
	
	
	
	// Récupère le patient en base et vérifie que le Diet connexté est bien son diet
	public Patient recuperationPatientEtVerificationAcces(Integer patientId, String dietEmail) {
		
		Patient patient = patientRepo.findById(patientId)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
		
		Dieteticien dietConnecte = dieteticienRepo.findByUtilisateurEmail(dietEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
		
		if (patient.getDieteticien() == null || !patient.getDieteticien().getId().equals(dietConnecte.getId())) {
	        throw new AccessDeniedException("Vous n'avez pas accès à ce patient");
	    }
		
		return patient;
		
	}
	
	

}

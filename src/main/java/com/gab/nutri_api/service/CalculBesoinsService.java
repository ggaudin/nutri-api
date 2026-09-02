package com.gab.nutri_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.BesoinsResponse;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.model.enums.GenrePatient;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;

@Service
public class CalculBesoinsService {
	
	@Autowired
	PatientRepository patientRepo;
	
	@Autowired
	DieteticienRepository dieteticienRepo;
	
	public BesoinsResponse getBesoins(Integer patientId, String dietEmail) {
		
		//Récupération des données du patient
		Patient patient = patientRepo.findById(patientId)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
		
		int age = calculAge(patient);
		
		
		//Diététicien connecté = diet du patient ?
		Dieteticien dietConnecte = dieteticienRepo.findByUtilisateurEmail(dietEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
		
		if (patient.getDieteticien() == null || !patient.getDieteticien().getId().equals(dietConnecte.getId())) {
	        throw new AccessDeniedException("Vous n'avez pas accès à ce patient");
	    }
		
			
		//Calcul du BEJ en Kj
		Integer bejKj = calculBEJKj(patient, age);
		
		//Calcul des besoins P, G, L
		BesoinsResponse besoins = new BesoinsResponse();
		
		besoins.setPatientId(patientId);
		besoins.setBej(calculBEJkcal(bejKj));
		besoins.setProteinesMin((int) Math.round(bejKj * 0.1 / 17));
		besoins.setProteinesMax((int) Math.round(bejKj * 0.2 / 17));
		besoins.setLipidesMin((int) Math.round(bejKj * 0.35 / 38));
		besoins.setLipidesMax((int) Math.round(bejKj * 0.4 / 38));
		besoins.setGlucidesMin((int) Math.round(bejKj * 0.4 / 17));
		besoins.setGlucidesMax((int) Math.round(bejKj * 0.55 / 17));
		
		
		return besoins;
	}
	
	
	private Integer calculBEJKj(Patient patient, int age) {
		
		double taille = patient.getTaille().doubleValue();
		double poids = patient.getPoids().doubleValue();
		double nap = patient.getNap().doubleValue();
		
		Integer bejKJ = null;
		// Cas des enfants/ados non pris en compte pour le moment, le BEJ sera null
		
		if (age > 17) {
			
			BigDecimal coef;
			
			if (patient.getGenre() == GenrePatient.HOMME) {
				coef = new BigDecimal("1.083");
			} else {
				coef = new BigDecimal("0.963");
			}
			
			//métabolisme de base avec formule Black et al.
			double mb = coef.doubleValue()
					* Math.pow(poids, 0.48)
		            * Math.pow(taille, 0.50)
		            * Math.pow(age, -0.13);
			
			
			bejKJ = (int) Math.round(mb * nap * 1000);
			
		}
		
		return bejKJ;
	}
	
	private Integer calculBEJkcal(Integer bejKJ) {
		return (int) Math.round(bejKJ / 4.184);
	}
	
	private int calculAge(Patient patient) {
		
		return Period.between(patient.getDate(), LocalDate.now()).getYears();
	}

}

package com.gab.nutri_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gab.nutri_api.dto.BesoinsResponse;
import com.gab.nutri_api.dto.DieteticienResponse;
import com.gab.nutri_api.dto.PatientListResponse;
import com.gab.nutri_api.dto.PatientResponse;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.service.CalculBesoinsService;
import com.gab.nutri_api.service.DieteticienService;

@RestController
@RequestMapping("/diet")
public class DieteticienController {
	
	@Autowired
	private DieteticienService dietService;
	
	@Autowired
	private CalculBesoinsService calculBesoinsService;
	
	//Récupère les infos personnelles du diet connecté
	@GetMapping("/profil")
    public DieteticienResponse getProfil(Authentication authentication) {
        return dietService.getProfil(authentication.getName());
    }
	
	//Récupère la liste des patients suivis par le diet connecté
	@GetMapping("/patients")
    public List<PatientListResponse> getPatients(Authentication authentication) {
        return dietService.getPatients(authentication.getName());
    }
	
	//Récupère les infos du patient identifié par l'id à condition que le diet connecté soit son diet
	@GetMapping("/patients/{id}")
    public PatientResponse getPatient(@PathVariable Integer id, Authentication authentication) {
        return dietService.getPatient(id, authentication.getName());
    }
	
	//Récupère les besoins nutritionnels d'un patient, calculés à partir de ses informations
	@GetMapping("/patients/{id}/besoins")
    public BesoinsResponse getBesoins(@PathVariable Integer id, Authentication authentication) {
        return calculBesoinsService.getBesoins(id, authentication.getName());
    }
	
	//Crée et enregistre en base un nouveau plan alimentaire pour le patient concerné
	@PostMapping("/patients/{id}/palim")
	public void creerPAlim(@PathVariable Integer id, Authentication authentication, @RequestBody PlanAlimentaire pAlim) {
		
		//TODO
		// Revoir objets utilisés et renvoyés
	}
	
	//Crée et enregistre en base un plan alimentaire générique que le diet pourra réutiliser comme modèle
	@PostMapping("/palim")
	public void creerPAlimGenerique(Authentication authentication, @RequestBody PlanAlimentaire pAlim) {
		
		//TODO
		// Revoir objets utilisés et renvoyés
	}
	
	//Récupère la liste des PAlim d'un patient
	@GetMapping("/patients/{id}/palim")
	public List<PlanAlimentaire> getPAlims(@PathVariable Integer id, Authentication authentication) {
	     return new ArrayList<PlanAlimentaire>();
	        
	      //TODO
			// Revoir objets utilisés et renvoyés
	    }
	
	

		
		
		
		
		
		
		
		
		
	//Ci-dessous endpoints à prévoir pour ajouter des possibilités de gestion au diet dans son espace
	//Càd mettre à jour / supprimer des patients ou des données / Créer des nouveaux patients
		//TODO si temps
	
	/**
	 * Delete - Supprimer un patient de la liste du diététicien.
	 * Ne supprime pas le compte du patient
	 * @param id - id du patient
	 */
//	@DeleteMapping("/patient/{id}")
//	public void deletePatient(@PathVariable("id") final Integer id) {
//		//TODO
//	}
//	
//	/**
//	 * Delete - Supprime le compte du diététicien.
//	 * Retire l'id du diététicien des patients associés.
//	 */
//	@DeleteMapping("/profil")
//	public void deleteDiet(@PathVariable("id") final Integer id) {
//		//TODO
//	}
	
//	/**
//	 * Update - Mise à jour des données du diététicien
//	 */
//	@PutMapping("/profil")
//	public Dieteticien updateEmployee(@PathVariable("id") final Integer id, @RequestBody Employee employee) {
//		//TODO
//	}
//
	
//	/**
//	 * Update - Mise à jour des données du patient par le diet
//	 */
//	@PutMapping("/patient/{id}")
//	public Dieteticien updateEmployee(@PathVariable("id") final Integer id, @RequestBody Employee employee) {
//		//TODO
//	}
	
//	/**
//	 * Create - Création d'un nouveau compte patient rattaché au diet connecté.
//	 */
//	@PostMapping("/patients")
//	public PatientResponse createPatient(@RequestBody CreatePatientRequest request, Authentication authentication) {
//
//	    return dietService.createPatient(request, authentication.getName());
//	}

}

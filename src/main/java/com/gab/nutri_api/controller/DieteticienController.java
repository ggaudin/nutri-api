package com.gab.nutri_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gab.nutri_api.dto.DieteticienResponse;
import com.gab.nutri_api.dto.PatientListResponse;
import com.gab.nutri_api.dto.PatientResponse;
import com.gab.nutri_api.service.auth.DieteticienService;

@RestController
@RequestMapping("/diet")
public class DieteticienController {
	
	@Autowired
	private DieteticienService dietService;
	
	@GetMapping("/profil")
    public DieteticienResponse getProfil(Authentication authentication) {
        return dietService.getProfil(authentication.getName());
    }
	
	@GetMapping("/patients")
    public List<PatientListResponse> getPatients(Authentication authentication) {
        return dietService.getPatients(authentication.getName());
    }
	
	@GetMapping("/patients/{id}")
    public PatientResponse getPatient(@PathVariable Integer id, Authentication authentication) {
        return dietService.getPatient(id, authentication.getName());
    }
	
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

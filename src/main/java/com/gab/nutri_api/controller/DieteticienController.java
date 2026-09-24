package com.gab.nutri_api.controller;

import java.util.List;

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
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireListResponse;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireResponse;
import com.gab.nutri_api.service.CalculBesoinsService;
import com.gab.nutri_api.service.DieteticienService;
import com.gab.nutri_api.service.planalimentaire.PlanAlimentaireService;

@RestController
@RequestMapping("/diet")
public class DieteticienController {

	private final DieteticienService dieteticienService;
	private final CalculBesoinsService calculBesoinsService;
	private final PlanAlimentaireService planAlimentaireService;

	public DieteticienController(DieteticienService dieteticienService, CalculBesoinsService calculBesoinsService,
			PlanAlimentaireService planAlimentaireService) {
		super();
		this.dieteticienService = dieteticienService;
		this.calculBesoinsService = calculBesoinsService;
		this.planAlimentaireService = planAlimentaireService;
	}

	// Récupère les infos personnelles du diet connecté
	@GetMapping("/profil")
	public DieteticienResponse getProfil(Authentication authentication) {
		return dieteticienService.getProfil(authentication.getName());
	}

	// Récupère la liste des patients suivis par le diet connecté
	@GetMapping("/patients")
	public List<PatientListResponse> getPatients(Authentication authentication) {
		return dieteticienService.getPatients(authentication.getName());
	}

	// Récupère les infos du patient identifié par l'id à condition que le diet
	// connecté soit son diet
	@GetMapping("/patients/{id}")
	public PatientResponse getPatient(@PathVariable Integer id, Authentication authentication) {
		return dieteticienService.getPatient(id, authentication.getName());
	}

	// Récupère les besoins nutritionnels d'un patient, calculés à partir de ses
	// informations
	@GetMapping("/patients/{id}/besoins")
	public BesoinsResponse getBesoins(@PathVariable Integer id, Authentication authentication) {
		return calculBesoinsService.getBesoins(id, authentication.getName());
	}

	// Crée et enregistre en base un nouveau plan alimentaire pour le patient
	// concerné
	@PostMapping("/patients/{id}/palim")
	public PlanAlimentaireResponse creerPlanAlimentaire(@PathVariable Integer id, Authentication authentication,
			@RequestBody PlanAlimentaireRequest planAlimentaireRequest) {

		return planAlimentaireService.creerPlanAlimentaire(id, authentication.getName(), planAlimentaireRequest);

	}

	// Crée et enregistre en base un plan alimentaire générique que le diet pourra
	// réutiliser comme modèle
	@PostMapping("/palim")
	public PlanAlimentaireResponse creerPlanAlimentaireTemplate(Authentication authentication,
			@RequestBody PlanAlimentaireRequest planAlimentaireRequest) {

		return planAlimentaireService.creerPlanAlimentaireTemplate(authentication.getName(), planAlimentaireRequest);

	}

	// Récupère la liste des Plans Alimentaires d'un patient
	@GetMapping("/patients/{id}/palim")
	public List<PlanAlimentaireListResponse> getPlansAlimentaires(@PathVariable Integer id,
			Authentication authentication) {

		return planAlimentaireService.getPlansAlimentaires(id, authentication.getName());

	}

	// Récupère les templates de plans alimentaires disponibles pour le diet
	@GetMapping("/palim-templates")
	public List<PlanAlimentaireListResponse> getPlansAlimentairesTemplates(Authentication authentication) {

		return planAlimentaireService.getPlansAlimentairesTemplates(authentication.getName());

	}

	// Ci-dessous endpoints à prévoir pour ajouter des possibilités de gestion au
	// diet dans son espace
	// Càd mettre à jour / supprimer des patients ou des données / Créer des
	// nouveaux patients
	// TODO

	/**
	 * Delete - Supprimer un patient de la liste du diététicien. Ne supprime pas le
	 * compte du patient
	 * 
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

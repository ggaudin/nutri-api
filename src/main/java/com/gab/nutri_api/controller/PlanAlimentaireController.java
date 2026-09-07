package com.gab.nutri_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireResponse;
import com.gab.nutri_api.service.PlanAlimentaireService;

@RestController
public class PlanAlimentaireController {

	@Autowired
	private PlanAlimentaireService planAlimentaireService;

	// Récupère un plan alimentaire à partir de son id
	// si personne connectée autorisée
	@GetMapping("/palim/{id}")
	public PlanAlimentaireResponse getPlanAlimentaireById(@PathVariable Integer id, Authentication authentication) {
		return planAlimentaireService.getPlanAlimentaireById(id, authentication.getName());
	}

	// Mise à jour plan alim si diet autorisé
	@PutMapping("/palim/{id}")
	public PlanAlimentaireResponse updatePlanAlimentaire(@PathVariable Integer planAlimentaireId, @RequestBody PlanAlimentaireRequest planAlimentaireRequest, Authentication authentication) {
		return planAlimentaireService.updatePlanAlimentaire(planAlimentaireId, planAlimentaireRequest, authentication.getName());
	}

	// Suppression plan alim si diet autorisé
	@DeleteMapping("/palim/{id}")
	public void deletePlanAlimentaire(@PathVariable Integer id, Authentication authentication) {
		planAlimentaireService.deletePlanAlimentaire(id, authentication.getName());
	}

}

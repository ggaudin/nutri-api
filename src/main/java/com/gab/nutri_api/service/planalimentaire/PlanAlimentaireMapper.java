package com.gab.nutri_api.service.planalimentaire;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gab.nutri_api.dto.planalimentaire.ApportsNutritionnels;
import com.gab.nutri_api.dto.planalimentaire.ComposantRepasResponse;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireListResponse;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireResponse;
import com.gab.nutri_api.dto.planalimentaire.RepasResponse;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;

@Component
public class PlanAlimentaireMapper {

	public PlanAlimentaireResponse planAlimentaireToResponse(PlanAlimentaire planAlimentaire,
			ApportsNutritionnels apportsNutritionnels) {

		PlanAlimentaireResponse planAlimentaireResponse = new PlanAlimentaireResponse();
		planAlimentaireResponse.setId(planAlimentaire.getId());
		planAlimentaireResponse.setNom(planAlimentaire.getNom());
		planAlimentaireResponse.setNotes(planAlimentaire.getNotes());

		if (planAlimentaire.getPatient() != null) {
			planAlimentaireResponse.setPatientId(planAlimentaire.getPatient().getId());
		}

		if (planAlimentaire.getDieteticien() != null) {
			planAlimentaireResponse.setDietId(planAlimentaire.getDieteticien().getId());
		}

		List<RepasResponse> listeRepasResponse = listeRepasToResponse(planAlimentaire);
		planAlimentaireResponse.setRepas(listeRepasResponse);

		if (apportsNutritionnels != null) {
			planAlimentaireResponse.setProteinesTot(apportsNutritionnels.getProteinesTot());
			planAlimentaireResponse.setGlucidesTot(apportsNutritionnels.getGlucidesTot());
			planAlimentaireResponse.setLipidesTot(apportsNutritionnels.getLipidesTot());
			planAlimentaireResponse.setEnergieTot(apportsNutritionnels.getEnergieTot());
		}

		return planAlimentaireResponse;

	}

	private List<RepasResponse> listeRepasToResponse(PlanAlimentaire planAlimentaire) {

		List<RepasResponse> listeRepasResponse = new ArrayList<RepasResponse>();

		for (Repas repas : planAlimentaire.getListeRepas()) {
			RepasResponse repasResponse = new RepasResponse();

			repasResponse.setNom(repas.getNom());
			repasResponse.setRang(repas.getRang());
			repasResponse.setId(repas.getId());

			List<ComposantRepasResponse> listeComposantsResponse = new ArrayList<ComposantRepasResponse>();

			for (ComposantRepas composantRepas : repas.getComposantsRepas()) {

				ComposantRepasResponse composantRepasResponse = new ComposantRepasResponse();

				composantRepasResponse.setId(composantRepas.getId());
				composantRepasResponse.setNom(composantRepas.getNom());
				composantRepasResponse.setAlimentId(composantRepas.getAliment().getId());
				composantRepasResponse.setQuantite(composantRepas.getQuantite());

				listeComposantsResponse.add(composantRepasResponse);
			}

			repasResponse.setComposantsRepas(listeComposantsResponse);

			listeRepasResponse.add(repasResponse);

		}

		return listeRepasResponse;

	}

	public List<PlanAlimentaireListResponse> toListePlanAlimentaireListResponse(
			List<PlanAlimentaire> listePlanAlimentaire) {

		List<PlanAlimentaireListResponse> listePlanAlimentaireResponse = new ArrayList<>();

		for (PlanAlimentaire planAlimentaire : listePlanAlimentaire) {

			PlanAlimentaireListResponse planAlimentaireListResponse = new PlanAlimentaireListResponse();
			planAlimentaireListResponse.setId(planAlimentaire.getId());
			planAlimentaireListResponse.setNom(planAlimentaire.getNom());
			listePlanAlimentaireResponse.add(planAlimentaireListResponse);

		}

		return listePlanAlimentaireResponse;
	}

}

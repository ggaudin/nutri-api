package com.gab.nutri_api.service.planalimentaire;

import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.planalimentaire.ComposantRepasRequest;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.RepasRequest;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.repository.AlimentRepository;

@Service
public class PlanAlimentaireConstructionService {
	
	private final AlimentRepository alimentRepository;

	public PlanAlimentaireConstructionService(AlimentRepository alimentRepository) {
		super();
		this.alimentRepository = alimentRepository;
	}
	
	public PlanAlimentaire constructionPlanAlimentaire(PlanAlimentaire planAlimentaire,
			PlanAlimentaireRequest planAlimentaireRequest) {

		planAlimentaire.setNom(planAlimentaireRequest.getNom());
		planAlimentaire.setNotes(planAlimentaireRequest.getNotes());

		for (RepasRequest repasRequest : planAlimentaireRequest.getRepas()) {

			Repas repas = construireRepas(repasRequest, planAlimentaire);
			planAlimentaire.ajouterRepas(repas);

		}

		return planAlimentaire;
	}

	public Repas construireRepas(RepasRequest repasRequest, PlanAlimentaire planAlimentaire) {
		Repas repas = new Repas();
		repas.setNom(repasRequest.getNom());
		repas.setRang(repasRequest.getRang());

		for (ComposantRepasRequest composantRepasRequest : repasRequest.getComposantsRepas()) {
			ComposantRepas composantRepas = construireComposantRepas(composantRepasRequest, repas);
			repas.ajouterComposantRepas(composantRepas);
		}

		return repas;
	}

	public ComposantRepas construireComposantRepas(ComposantRepasRequest composantRepasRequest, Repas repas) {
		ComposantRepas composantRepas = new ComposantRepas();
		composantRepas.setNom(composantRepasRequest.getNom());
		composantRepas.setQuantite(composantRepasRequest.getQuantite());

		Aliment aliment = alimentRepository.findById(composantRepasRequest.getAlimentId()).orElseThrow(
				() -> new RuntimeException("Aliment non trouvé pour l'id : " + composantRepasRequest.getAlimentId()));

		composantRepas.setAliment(aliment);

		return composantRepas;
	}

}

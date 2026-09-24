package com.gab.nutri_api.service.planalimentaire;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.RepasRequest;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.repository.AlimentRepository;

@Service
public class PlanAlimentaireUpdateService {

	private final PlanAlimentaireConstructionService planAlimentaireConstructionService;
	private final AlimentRepository alimentRepository;

	public PlanAlimentaireUpdateService(PlanAlimentaireConstructionService planAlimentaireConstructionService,
			AlimentRepository alimentRepository) {
		super();
		this.planAlimentaireConstructionService = planAlimentaireConstructionService;
		this.alimentRepository = alimentRepository;
	}

	public PlanAlimentaire miseAJourPlanAlimentaire(PlanAlimentaire planAlimentaire,
			PlanAlimentaireRequest planAlimentaireRequest) {

		planAlimentaire.setNom(planAlimentaireRequest.getNom());
		planAlimentaire.setNotes(planAlimentaireRequest.getNotes());

		mettreAJour(planAlimentaire, planAlimentaireRequest);

		return planAlimentaire;
	}

	private void mettreAJour(PlanAlimentaire planAlimentaire, PlanAlimentaireRequest planAlimentaireRequest) {

		mettreAJourLesRepas(planAlimentaire, planAlimentaireRequest);

	}

	private void mettreAJourLesRepas(PlanAlimentaire planAlimentaire, PlanAlimentaireRequest planAlimentaireRequest) {

		
		// Mise à jour et création
		planAlimentaireRequest.getRepas().forEach(repasRequest -> {

			// Création
			if (repasRequest.getId() == null) {
				Repas repas = planAlimentaireConstructionService.construireRepas(repasRequest, planAlimentaire);
				planAlimentaire.ajouterRepas(repas);
				return;
			}

			// Recherche du repas existant
			Repas repas = planAlimentaire.getListeRepas().stream()
					.filter(rep -> Objects.equals(rep.getId(), repasRequest.getId())).findFirst()
					.orElseThrow(() -> new RuntimeException(
							"Repas non trouvé pour l'id : " + repasRequest.getId()));

			// Mise à jour
			repas.setNom(repasRequest.getNom());
			repas.setRang(repasRequest.getRang());
			mettreAJourLesComposantsRepas(repas, repasRequest);
		});

		// Suppression
		planAlimentaire.getListeRepas().stream()
				.filter(repas -> planAlimentaireRequest.getRepas().stream()
						.noneMatch(repasRequest -> Objects.equals(repas.getId(), repasRequest.getId())))
				.toList().forEach(repas -> planAlimentaire.supprimerRepas(repas));

	}

	private void mettreAJourLesComposantsRepas(Repas repas, RepasRequest repasRequest) {

		// Mise à jour et création
		repasRequest.getComposantsRepas().forEach(composantRepasRequest -> {

			// Création
			if (composantRepasRequest.getId() == null) {
				ComposantRepas composantRepas = planAlimentaireConstructionService
						.construireComposantRepas(composantRepasRequest, repas);
				repas.ajouterComposantRepas(composantRepas);
				return;
			}

			// Recherche du composant existant
			ComposantRepas composantRepas = repas.getComposantsRepas().stream()
					.filter(composant -> Objects.equals(composant.getId(), composantRepasRequest.getId())).findFirst()
					.orElseThrow(() -> new RuntimeException(
							"ComposantRepas non trouvé pour l'id : " + composantRepasRequest.getId()));

			// Mise à jour
			composantRepas.setNom(composantRepasRequest.getNom());
			composantRepas.setQuantite(composantRepasRequest.getQuantite());

			if (composantRepas.getAliment() == null
					|| !Objects.equals(composantRepas.getAliment().getId(), composantRepasRequest.getAlimentId())) {

				Aliment aliment = alimentRepository.findById(composantRepasRequest.getAlimentId())
						.orElseThrow(() -> new RuntimeException(
								"Aliment non trouvé pour l'id : " + composantRepasRequest.getAlimentId()));

				composantRepas.setAliment(aliment);
			}
		});

		// Suppression
		repas.getComposantsRepas().stream()
				.filter(composantRepas -> repasRequest.getComposantsRepas().stream().noneMatch(
						composantRepasRequest -> Objects.equals(composantRepas.getId(), composantRepasRequest.getId())))
				.toList().forEach(composantRepas -> repas.supprimerComposantRepas(composantRepas));
	}

}

package com.gab.nutri_api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.AlimentResponse;
import com.gab.nutri_api.dto.planalimentaire.ComposantRepasRequest;
import com.gab.nutri_api.dto.planalimentaire.ComposantRepasResponse;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireListResponse;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireResponse;
import com.gab.nutri_api.dto.planalimentaire.RepasRequest;
import com.gab.nutri_api.dto.planalimentaire.RepasResponse;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.model.Utilisateur;
import com.gab.nutri_api.model.enums.RoleUtilisateur;
import com.gab.nutri_api.model.enums.TypePlan;
import com.gab.nutri_api.model.enums.VisibilitePlan;
import com.gab.nutri_api.repository.AlimentRepository;
import com.gab.nutri_api.repository.DieteticienRepository;
import com.gab.nutri_api.repository.PatientRepository;
import com.gab.nutri_api.repository.PlanAlimentaireRepository;
import com.gab.nutri_api.repository.UtilisateurRepository;

@Service
public class PlanAlimentaireService {

	@Autowired
	private PlanAlimentaireRepository planAlimentaireRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DieteticienRepository dieteticienRepository;

	@Autowired
	private AlimentRepository alimentRepository;

	@Autowired
	private AlimentService alimentService;

	@Autowired
	private DieteticienService dieteticienService;
	
	

	public PlanAlimentaireResponse getPlanAlimentaireById(Integer planAlimentaireId, String utilisateurEmail) {

		PlanAlimentaire planAlimentaire = planAlimentaireRepository.findById(planAlimentaireId).orElseThrow(
				() -> new RuntimeException("Plan Alimentaire non trouvé pour l'id : " + planAlimentaireId));

		verificationsAccesPlanAlimentaire(planAlimentaire, utilisateurEmail);

		PlanAlimentaireResponse planAlimentaireResponse = constructionPlanAlimentaireResponse(planAlimentaire);

		return planAlimentaireResponse;
	}

	public PlanAlimentaireResponse updatePlanAlimentaire(Integer planAlimentaireId,
			PlanAlimentaireRequest planAlimentaireRequest, String utilisateurEmail) {

		Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Utilisateur introuvable pour l'email :" + utilisateurEmail));

		if (utilisateur.getRole() == RoleUtilisateur.PATIENT || utilisateur.getRole() == RoleUtilisateur.ADMIN) {
			throw new AccessDeniedException("Vous ne pouvez pas modifier ce plan alimentaire");
		}

		PlanAlimentaire planAlimentaire = planAlimentaireRepository.findById(planAlimentaireId).orElseThrow(
				() -> new RuntimeException("Plan Alimentaire non trouvé pour l'id : " + planAlimentaireId));

		verificationAccesPlanParDiet(utilisateurEmail, planAlimentaire);

		planAlimentaire = miseAJourPlanAlimentaire(planAlimentaire, planAlimentaireRequest);

		planAlimentaireRepository.save(planAlimentaire);

		return constructionPlanAlimentaireResponse(planAlimentaire);

	}

	public void deletePlanAlimentaire(Integer planAlimentaireId, String utilisateurEmail) {

		Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Utilisateur introuvable pour l'email :" + utilisateurEmail));

		if (utilisateur.getRole() == RoleUtilisateur.PATIENT || utilisateur.getRole() == RoleUtilisateur.ADMIN) {
			throw new AccessDeniedException("Vous ne pouvez pas modifier ce plan alimentaire");
		}

		PlanAlimentaire planAlimentaire = planAlimentaireRepository.findById(planAlimentaireId).orElseThrow(
				() -> new RuntimeException("Plan Alimentaire non trouvé pour l'id : " + planAlimentaireId));

		verificationAccesPlanParDiet(utilisateurEmail, planAlimentaire);

		planAlimentaireRepository.delete(planAlimentaire);

	}

	public PlanAlimentaireResponse creerPlanAlimentaire(Integer patientId, String dietEmail,
			PlanAlimentaireRequest planAlimentaireRequest) {

		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		Dieteticien dietConnecte = dieteticienRepository.findByUtilisateurEmail(dietEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		if (patient.getDieteticien() == null || !patient.getDieteticien().getId().equals(dietConnecte.getId())) {
			throw new AccessDeniedException("Vous n'avez pas accès à ce patient");
		}

		PlanAlimentaire planAlimentaire = new PlanAlimentaire();
		planAlimentaire = constructionPlanAlimentaire(planAlimentaire, planAlimentaireRequest);

		planAlimentaire.setPatient(patient);
		planAlimentaire.setDieteticien(dietConnecte);

		planAlimentaireRepository.save(planAlimentaire);

		return constructionPlanAlimentaireResponse(planAlimentaire);
	}

	public PlanAlimentaireResponse creerPlanAlimentaireTemplate(String dietEmail,
			PlanAlimentaireRequest planAlimentaireRequest) {

		Dieteticien dietConnecte = dieteticienRepository.findByUtilisateurEmail(dietEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		PlanAlimentaire planAlimentaire = new PlanAlimentaire();
		planAlimentaire = constructionPlanAlimentaire(planAlimentaire, planAlimentaireRequest);

		planAlimentaire.setType(TypePlan.TEMPLATE);
		planAlimentaire.setDieteticien(dietConnecte);

		planAlimentaireRepository.save(planAlimentaire);

		return constructionPlanAlimentaireResponse(planAlimentaire);
	}

	public List<PlanAlimentaireListResponse> getPlansAlimentaires(Integer patientId, String dietEmail) {

		dieteticienService.recuperationPatientEtVerificationAcces(patientId, dietEmail);

		List<PlanAlimentaire> listePlanAlimentaire = planAlimentaireRepository.findByPatientId(patientId);

		return construireListePlanAlimentaireListResponse(listePlanAlimentaire);
	}

	public List<PlanAlimentaireListResponse> getPlansAlimentairesTemplates(String dietEmail) {

		Dieteticien diet = dieteticienRepository.findByUtilisateurEmail(dietEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Dieteticien introuvable pour l'email :" + dietEmail));

		List<PlanAlimentaire> listePlanAlimentaire = planAlimentaireRepository
				.findByDieteticienIdAndTypeOrVisibilite(diet.getId(), TypePlan.TEMPLATE, VisibilitePlan.GLOBAL);

		return construireListePlanAlimentaireListResponse(listePlanAlimentaire);
	}

	private void verificationsAccesPlanAlimentaire(PlanAlimentaire planAlimentaire, String utilisateurEmail) {

		Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Utilisateur introuvable pour l'email :" + utilisateurEmail));

		if (utilisateur.getRole() == RoleUtilisateur.PATIENT) {
			Patient patient = patientRepository.findByUtilisateurEmail(utilisateurEmail).orElseThrow(
					() -> new UsernameNotFoundException("Patient introuvable pour l'email :" + utilisateurEmail));

			if (planAlimentaire.getPatient() == null || !planAlimentaire.getPatient().getId().equals(patient.getId())) {
				throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
			}

		} else if (utilisateur.getRole() == RoleUtilisateur.DIETETICIEN) {

			verificationAccesPlanParDiet(utilisateurEmail, planAlimentaire);

		} else { // Si c'est un admin (n'a pas accès aux données de santé des patients)

			if (planAlimentaire.getDieteticien() != null || planAlimentaire.getPatient() != null) {
				throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
			}

		}
	}

	private void verificationAccesPlanParDiet(String utilisateurEmail, PlanAlimentaire planAlimentaire) {

		Dieteticien diet = dieteticienRepository.findByUtilisateurEmail(utilisateurEmail).orElseThrow(
				() -> new UsernameNotFoundException("Dieteticien introuvable pour l'email :" + utilisateurEmail));

		// Si le plan appartient à un patient sans diet -> Accès refusé
		if (planAlimentaire.getDieteticien() == null && planAlimentaire.getPatient() != null) {
			throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
		}
		// Si le plan appartient à un autre diététicien -> Accès refusé
		if (planAlimentaire.getDieteticien() != null
				&& !planAlimentaire.getDieteticien().getId().equals(diet.getId())) {
			throw new AccessDeniedException("Vous n'avez pas accès à ce plan alimentaire");
		}
	}

	private PlanAlimentaireResponse constructionPlanAlimentaireResponse(PlanAlimentaire planAlimentaire) {

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

		List<RepasResponse> listeRepasResponse = constructionListeRepasResponse(planAlimentaire);
		planAlimentaireResponse.setRepas(listeRepasResponse);

		calculApportsJournaliers(planAlimentaireResponse);

		return planAlimentaireResponse;

	}

	private List<RepasResponse> constructionListeRepasResponse(PlanAlimentaire planAlimentaire) {

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

	private void calculApportsJournaliers(PlanAlimentaireResponse planAlimentaireResponse) {

		BigDecimal proteines = new BigDecimal("0");
		BigDecimal glucides = new BigDecimal("0");
		BigDecimal lipides = new BigDecimal("0");
		BigDecimal energie = new BigDecimal("0");

		for (RepasResponse repas : planAlimentaireResponse.getRepas()) {
			for (ComposantRepasResponse composantRepas : repas.getComposantsRepas()) {
				BigDecimal quantite = composantRepas.getQuantite();
				AlimentResponse alimentResponse = alimentService.getAlimentById(composantRepas.getAlimentId());

				proteines = proteines.add(alimentResponse.getProteines()
						.multiply(quantite.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
				glucides = glucides.add(alimentResponse.getGlucides()
						.multiply(quantite.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
				lipides = lipides.add(alimentResponse.getLipides()
						.multiply(quantite.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
			}
		}

		BigDecimal energieProt = proteines.multiply(BigDecimal.valueOf(4));
		BigDecimal energieGlucides = glucides.multiply(BigDecimal.valueOf(4));
		BigDecimal energieLipides = lipides.multiply(BigDecimal.valueOf(9));

		// energie en kcal
		energie = energie.add(energieProt).add(energieGlucides).add(energieLipides);

		planAlimentaireResponse.setProteinesTot(proteines);
		planAlimentaireResponse.setGlucidesTot(glucides);
		planAlimentaireResponse.setLipidesTot(lipides);
		planAlimentaireResponse.setEnergieTot(energie);

	}

	private PlanAlimentaire miseAJourPlanAlimentaire(PlanAlimentaire planAlimentaire,
			PlanAlimentaireRequest planAlimentaireRequest) {

		planAlimentaire.setNom(planAlimentaireRequest.getNom());
		planAlimentaire.setNotes(planAlimentaireRequest.getNotes());

		synchroniserLesRepas(planAlimentaire, planAlimentaireRequest);

		return planAlimentaire;
	}

	private void synchroniserLesRepas(PlanAlimentaire planAlimentaire, PlanAlimentaireRequest planAlimentaireRequest) {

		// Mise à jour et création
		for (RepasRequest repasRequest : planAlimentaireRequest.getRepas()) {

			boolean existeEnBase = false;

			for (Repas repas : planAlimentaire.getListeRepas()) {

				if (repasRequest.getId() != null && Objects.equals(repasRequest.getId(), repas.getId())) {

					existeEnBase = true;

					repas.setNom(repasRequest.getNom());
					repas.setRang(repasRequest.getRang());
					synchroniserLesComposantsRepas(repas, repasRequest);
					break;
				}
			}

			if (!existeEnBase) {
				Repas repas = construireRepas(repasRequest, planAlimentaire);
				planAlimentaire.ajouterRepas(repas);
			}
		}

		
		// Suppression
		planAlimentaire.getListeRepas().removeIf(repas -> planAlimentaireRequest.getRepas().stream()
			.noneMatch(repasRequest -> Objects.equals(repas.getId(), repasRequest.getId())));
		
	}

	private void synchroniserLesComposantsRepas(Repas repas, RepasRequest repasRequest) {

		// Mise à jour et création
		for (ComposantRepasRequest composantRepasRequest : repasRequest.getComposantsRepas()) {

			boolean existeEnBase = false;

			for (ComposantRepas composantRepas : repas.getComposantsRepas()) {

				if (composantRepasRequest.getId() != null && Objects.equals(composantRepasRequest.getId(), composantRepas.getId())) {

					existeEnBase = true;

					composantRepas.setNom(composantRepasRequest.getNom());
					composantRepas.setQuantite(composantRepasRequest.getQuantite());
					
					if (composantRepas.getAliment() == null || !Objects.equals(composantRepas.getAliment().getId(), composantRepasRequest.getAlimentId())) {
						Aliment aliment = alimentRepository.findById(composantRepasRequest.getAlimentId()).orElseThrow(
								() -> new RuntimeException("Aliment non trouvé pour l'id : " + composantRepasRequest.getAlimentId()));

						composantRepas.setAliment(aliment);
					}
					break;
				}
			}

			if (!existeEnBase) {
				ComposantRepas composantRepas = construireComposantRepas(composantRepasRequest, repas);
				repas.ajouterComposantRepas(composantRepas);
			}
		}

		// Suppression
		repas.getComposantsRepas().removeIf(composantRepas -> repasRequest.getComposantsRepas().stream()
				.noneMatch(composantRepasRequest -> Objects.equals(composantRepas.getId(), composantRepasRequest.getId())));
			
	}

	private PlanAlimentaire constructionPlanAlimentaire(PlanAlimentaire planAlimentaire,
			PlanAlimentaireRequest planAlimentaireRequest) {

		planAlimentaire.setNom(planAlimentaireRequest.getNom());
		planAlimentaire.setNotes(planAlimentaireRequest.getNotes());

		for (RepasRequest repasRequest : planAlimentaireRequest.getRepas()) {

			Repas repas = construireRepas(repasRequest, planAlimentaire);
			planAlimentaire.ajouterRepas(repas);

		}

		return planAlimentaire;
	}

	private Repas construireRepas(RepasRequest repasRequest, PlanAlimentaire planAlimentaire) {
		Repas repas = new Repas();
		repas.setNom(repasRequest.getNom());
		repas.setRang(repasRequest.getRang());

		for (ComposantRepasRequest composantRepasRequest : repasRequest.getComposantsRepas()) {
			ComposantRepas composantRepas = construireComposantRepas(composantRepasRequest, repas);
			repas.ajouterComposantRepas(composantRepas);
		}

		return repas;
	}

	private ComposantRepas construireComposantRepas(ComposantRepasRequest composantRepasRequest, Repas repas) {
		ComposantRepas composantRepas = new ComposantRepas();
		composantRepas.setNom(composantRepasRequest.getNom());
		composantRepas.setQuantite(composantRepasRequest.getQuantite());

		Aliment aliment = alimentRepository.findById(composantRepasRequest.getAlimentId()).orElseThrow(
				() -> new RuntimeException("Aliment non trouvé pour l'id : " + composantRepasRequest.getAlimentId()));

		composantRepas.setAliment(aliment);

		return composantRepas;
	}

	private List<PlanAlimentaireListResponse> construireListePlanAlimentaireListResponse(
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

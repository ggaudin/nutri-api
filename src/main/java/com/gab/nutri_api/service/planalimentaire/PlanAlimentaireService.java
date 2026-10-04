package com.gab.nutri_api.service.planalimentaire;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gab.nutri_api.dto.PatientDieteticien;
import com.gab.nutri_api.dto.planalimentaire.ApportsNutritionnels;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireListResponse;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireResponse;
import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.model.enums.TypePlan;
import com.gab.nutri_api.model.enums.VisibilitePlan;
import com.gab.nutri_api.repository.PlanAlimentaireRepository;
import com.gab.nutri_api.repository.RepasRepository;
import com.gab.nutri_api.service.AccessService;
import com.gab.nutri_api.service.DieteticienService;

@Service
public class PlanAlimentaireService {

	private final PlanAlimentaireRepository planAlimentaireRepository;
	private final RepasRepository repasRepository;
	private final DieteticienService dieteticienService;
	private final PlanAlimentaireUpdateService planAlimentaireUpdateService;
	private final PlanAlimentaireMapper planAlimentaireMapper;
	private final PlanAlimentaireConstructionService planAlimentaireConstructionService;
	private final PlanAlimentaireNutritionService planAlimentaireNutritionService;
	private final AccessService planAlimentaireAccessService;

	public PlanAlimentaireService(PlanAlimentaireRepository planAlimentaireRepository,
			RepasRepository repasRepository,
			DieteticienService dieteticienService, PlanAlimentaireUpdateService planAlimentaireUpdateService,
			PlanAlimentaireMapper planAlimentaireMapper,
			PlanAlimentaireConstructionService planAlimentaireConstructionService,
			PlanAlimentaireNutritionService planAlimentaireNutritionService,
			AccessService planAlimentaireAccessService) {
		super();
		this.planAlimentaireRepository = planAlimentaireRepository;
		this.repasRepository = repasRepository;
		this.dieteticienService = dieteticienService;
		this.planAlimentaireUpdateService = planAlimentaireUpdateService;
		this.planAlimentaireMapper = planAlimentaireMapper;
		this.planAlimentaireConstructionService = planAlimentaireConstructionService;
		this.planAlimentaireNutritionService = planAlimentaireNutritionService;
		this.planAlimentaireAccessService = planAlimentaireAccessService;

	}
	
	@Transactional (readOnly = true)
	public PlanAlimentaireResponse getPlanAlimentaireById(Integer planAlimentaireId, String utilisateurEmail) {

		PlanAlimentaire planAlimentaire = planAlimentaireRepository.findByIdWithPatientAndDieteticien(planAlimentaireId).orElseThrow(
				() -> new RuntimeException("Plan Alimentaire non trouvé pour l'id : " + planAlimentaireId));

		planAlimentaireAccessService.verificationsAccesPlanAlimentaire(planAlimentaire, utilisateurEmail);
		
		List<Repas> repas = repasRepository.findByPlanIdWithComposantsRepasAndAliment(planAlimentaireId);

		ApportsNutritionnels apportsNutritionnels = planAlimentaireNutritionService.calculApportsJournaliers(planAlimentaire, repas);

		PlanAlimentaireResponse planAlimentaireResponse = planAlimentaireMapper
				.planAlimentaireToResponse(planAlimentaire, repas, apportsNutritionnels);

		return planAlimentaireResponse;
	}

	@Transactional
	public PlanAlimentaireResponse updatePlanAlimentaire(Integer planAlimentaireId,
			PlanAlimentaireRequest planAlimentaireRequest, String utilisateurEmail) {

		planAlimentaireAccessService.verificationRole(utilisateurEmail);

		PlanAlimentaire planAlimentaire = planAlimentaireRepository.findByIdWithPatientAndDieteticien(planAlimentaireId).orElseThrow(
				() -> new RuntimeException("Plan Alimentaire non trouvé pour l'id : " + planAlimentaireId));

		planAlimentaireAccessService.verificationAccesPlanParDiet(utilisateurEmail, planAlimentaire);
		
		List<Repas> repas = repasRepository.findByPlanIdWithComposantsRepasAndAliment(planAlimentaireId);

		planAlimentaire = planAlimentaireUpdateService.miseAJourPlanAlimentaire(planAlimentaire, repas,
				planAlimentaireRequest);

		planAlimentaire = planAlimentaireRepository.save(planAlimentaire);
		
		repas = repasRepository.findByPlanIdWithComposantsRepasAndAliment(planAlimentaireId);

		ApportsNutritionnels apportsNutritionnels = planAlimentaireNutritionService
				.calculApportsJournaliers(planAlimentaire, repas);

		PlanAlimentaireResponse planAlimentaireResponse = planAlimentaireMapper
				.planAlimentaireToResponse(planAlimentaire, repas, apportsNutritionnels);

		return planAlimentaireResponse;

	}

	@Transactional
	public void deletePlanAlimentaire(Integer planAlimentaireId, String utilisateurEmail) {

		planAlimentaireAccessService.verificationRole(utilisateurEmail);

		PlanAlimentaire planAlimentaire = planAlimentaireRepository.findById(planAlimentaireId).orElseThrow(
				() -> new RuntimeException("Plan Alimentaire non trouvé pour l'id : " + planAlimentaireId));

		planAlimentaireAccessService.verificationAccesPlanParDiet(utilisateurEmail, planAlimentaire);

		planAlimentaireRepository.delete(planAlimentaire);

	}

	@Transactional
	public PlanAlimentaireResponse creerPlanAlimentaire(Integer patientId, String dietEmail,
			PlanAlimentaireRequest planAlimentaireRequest) {

		PatientDieteticien patientDieteticien = planAlimentaireAccessService
				.recuperationPatientDieteticienEtVerificationAcces(patientId, dietEmail);

		PlanAlimentaire planAlimentaire = new PlanAlimentaire();
		planAlimentaire = planAlimentaireConstructionService.constructionPlanAlimentaire(planAlimentaire,
				planAlimentaireRequest);

		planAlimentaire.setPatient(patientDieteticien.patient());
		planAlimentaire.setDieteticien(patientDieteticien.dieteticien());

		planAlimentaire = planAlimentaireRepository.save(planAlimentaire);

		ApportsNutritionnels apportsNutritionnels = planAlimentaireNutritionService
				.calculApportsJournaliers(planAlimentaire, planAlimentaire.getListeRepas());

		PlanAlimentaireResponse planAlimentaireResponse = planAlimentaireMapper
				.planAlimentaireToResponse(planAlimentaire, planAlimentaire.getListeRepas(), apportsNutritionnels);

		return planAlimentaireResponse;
	}

	@Transactional
	public PlanAlimentaireResponse creerPlanAlimentaireTemplate(String dietEmail,
			PlanAlimentaireRequest planAlimentaireRequest) {

		Dieteticien dietConnecte = dieteticienService.getDieteticien(dietEmail);

		PlanAlimentaire planAlimentaire = new PlanAlimentaire();
		planAlimentaire = planAlimentaireConstructionService.constructionPlanAlimentaire(planAlimentaire,
				planAlimentaireRequest);

		planAlimentaire.setType(TypePlan.TEMPLATE);
		planAlimentaire.setDieteticien(dietConnecte);

		planAlimentaire = planAlimentaireRepository.save(planAlimentaire);

		ApportsNutritionnels apportsNutritionnels = planAlimentaireNutritionService
				.calculApportsJournaliers(planAlimentaire, planAlimentaire.getListeRepas());

		PlanAlimentaireResponse planAlimentaireResponse = planAlimentaireMapper
				.planAlimentaireToResponse(planAlimentaire, planAlimentaire.getListeRepas(), apportsNutritionnels);

		return planAlimentaireResponse;
	}

	public List<PlanAlimentaireListResponse> getPlansAlimentaires(Integer patientId, String dietEmail) {

		planAlimentaireAccessService.recuperationPatientDieteticienEtVerificationAcces(patientId, dietEmail);

		List<PlanAlimentaire> listePlanAlimentaire = planAlimentaireRepository.findByPatientId(patientId);

		return planAlimentaireMapper.toListePlanAlimentaireListResponse(listePlanAlimentaire);
	}

	public List<PlanAlimentaireListResponse> getPlansAlimentairesTemplates(String dietEmail) {

		Dieteticien diet = dieteticienService.getDieteticien(dietEmail);

		List<PlanAlimentaire> listePlanAlimentaire = planAlimentaireRepository
				.findByDieteticienIdAndTypeOrVisibilite(diet.getId(), TypePlan.TEMPLATE, VisibilitePlan.GLOBAL);

		return planAlimentaireMapper.toListePlanAlimentaireListResponse(listePlanAlimentaire);
	}

}

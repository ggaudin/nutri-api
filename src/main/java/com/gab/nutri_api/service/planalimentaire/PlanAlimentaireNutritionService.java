package com.gab.nutri_api.service.planalimentaire;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.AlimentResponse;
import com.gab.nutri_api.dto.planalimentaire.ApportsNutritionnels;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.repository.ComposantRepasRepository;
import com.gab.nutri_api.service.AlimentService;

@Service
public class PlanAlimentaireNutritionService {

	private final AlimentService alimentService;
	private final ComposantRepasRepository composantRepasRepository;

	public PlanAlimentaireNutritionService(AlimentService alimentService,
			ComposantRepasRepository composantRepasRepository) {
		super();
		this.alimentService = alimentService;
		this.composantRepasRepository = composantRepasRepository;
	}

	public ApportsNutritionnels calculApportsJournaliers(PlanAlimentaire planAlimentaire) {

		List<ComposantRepas> composantsRepas = composantRepasRepository
				.findComposantsWithAlimentByPlanId(planAlimentaire.getId());

		BigDecimal proteines = new BigDecimal("0");
		BigDecimal glucides = new BigDecimal("0");
		BigDecimal lipides = new BigDecimal("0");
		BigDecimal energie = new BigDecimal("0");

		for (ComposantRepas composantRepas : composantsRepas) {
			BigDecimal quantite = composantRepas.getQuantite();
			AlimentResponse alimentResponse = alimentService.alimentToResponse(composantRepas.getAliment());

			proteines = proteines.add(alimentResponse.getProteines()
					.multiply(quantite.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
			glucides = glucides.add(alimentResponse.getGlucides()
					.multiply(quantite.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
			lipides = lipides.add(alimentResponse.getLipides()
					.multiply(quantite.divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP)));
		}

		BigDecimal energieProt = proteines.multiply(BigDecimal.valueOf(4));
		BigDecimal energieGlucides = glucides.multiply(BigDecimal.valueOf(4));
		BigDecimal energieLipides = lipides.multiply(BigDecimal.valueOf(9));

		// energie en kcal
		energie = energie.add(energieProt).add(energieGlucides).add(energieLipides);

		ApportsNutritionnels apportsNutritionnels = new ApportsNutritionnels();

		apportsNutritionnels.setProteinesTot(proteines);
		apportsNutritionnels.setGlucidesTot(glucides);
		apportsNutritionnels.setLipidesTot(lipides);
		apportsNutritionnels.setEnergieTot(energie);

		return apportsNutritionnels;

	}

}

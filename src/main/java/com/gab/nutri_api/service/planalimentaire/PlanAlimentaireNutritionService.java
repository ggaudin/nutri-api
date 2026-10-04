package com.gab.nutri_api.service.planalimentaire;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.planalimentaire.ApportsNutritionnels;
import com.gab.nutri_api.dto.planalimentaire.MacronutrimentProjection;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.repository.CompositionAlimentRepository;

@Service
public class PlanAlimentaireNutritionService {

	private final CompositionAlimentRepository compositionAlimentRepository;

	public PlanAlimentaireNutritionService(CompositionAlimentRepository compositionAlimentRepository) {
		super();
		this.compositionAlimentRepository = compositionAlimentRepository;
	}

	public ApportsNutritionnels calculApportsJournaliers(PlanAlimentaire planAlimentaire, List<Repas> repas) {

		List<ComposantRepas> composantsRepas = repas.stream().flatMap(r -> r.getComposantsRepas().stream()).toList();

		List<Long> alimentIds = composantsRepas.stream().map(composant -> composant.getAliment().getId()).distinct()
				.toList();

		List<MacronutrimentProjection> macronutrimentProjections = compositionAlimentRepository
				.findMacronutrimentsByAlimentIds(alimentIds);

		Map<Long, Map<Integer, BigDecimal>> mapMacronutriments = macronutrimentProjections.stream()
				.collect(Collectors.groupingBy(projection -> projection.alimentId(),
						Collectors.toMap(projection -> projection.code(),
								projection -> Objects.requireNonNullElse(projection.valeur(), BigDecimal.ZERO))));

		BigDecimal proteines = new BigDecimal("0");
		BigDecimal glucides = new BigDecimal("0");
		BigDecimal lipides = new BigDecimal("0");
		BigDecimal energie = new BigDecimal("0");

		for (ComposantRepas composantRepas : composantsRepas) {
			BigDecimal quantite = composantRepas.getQuantite();

			if (quantite == null) {
				quantite = BigDecimal.ZERO;
			}

			Long alimentId = composantRepas.getAliment().getId();

			Map<Integer, BigDecimal> macronutriments = mapMacronutriments.getOrDefault(alimentId,
					Collections.emptyMap());

			BigDecimal proteinesValeur = macronutriments.getOrDefault(25000, BigDecimal.ZERO);

			BigDecimal glucidesValeur = macronutriments.getOrDefault(31000, BigDecimal.ZERO);

			BigDecimal lipidesValeur = macronutriments.getOrDefault(40000, BigDecimal.ZERO);

			proteines = proteines
					.add(proteinesValeur.multiply(quantite).divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP));
			glucides = glucides
					.add(glucidesValeur.multiply(quantite).divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP));
			lipides = lipides
					.add(lipidesValeur.multiply(quantite).divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP));
		}

		BigDecimal energieProt = proteines.multiply(BigDecimal.valueOf(4));
		BigDecimal energieGlucides = glucides.multiply(BigDecimal.valueOf(4));
		BigDecimal energieLipides = lipides.multiply(BigDecimal.valueOf(9));

		// energie en kcal
		energie = energie.add(energieProt).add(energieGlucides).add(energieLipides);

		ApportsNutritionnels apportsNutritionnels = new ApportsNutritionnels(proteines, glucides, lipides, energie);

		return apportsNutritionnels;

	}

}

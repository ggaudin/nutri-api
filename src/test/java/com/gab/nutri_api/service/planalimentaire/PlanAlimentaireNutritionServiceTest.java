package com.gab.nutri_api.service.planalimentaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gab.nutri_api.dto.planalimentaire.ApportsNutritionnels;
import com.gab.nutri_api.dto.planalimentaire.MacronutrimentProjection;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.ComposantRepas;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.repository.CompositionAlimentRepository;

@ExtendWith(MockitoExtension.class)
public class PlanAlimentaireNutritionServiceTest {

	@Mock
	private CompositionAlimentRepository compositionAlimentRepository;

	@InjectMocks
	private PlanAlimentaireNutritionService nutritionService;

	@Test
	void calculApportsJournaliers_calculeCorrectementLesMacronutriments() {

		Aliment aliment = new Aliment();
		aliment.setId(1L);

		ComposantRepas composant = new ComposantRepas();
		composant.setAliment(aliment);
		composant.setQuantite(new BigDecimal("200"));

		Repas repas = new Repas();
		repas.ajouterComposantRepas(composant);

		List<MacronutrimentProjection> projections = List.of(
				new MacronutrimentProjection(1L, 25000, new BigDecimal("10")),
				new MacronutrimentProjection(1L, 31000, new BigDecimal("20")),
				new MacronutrimentProjection(1L, 40000, new BigDecimal("5")));

		when(compositionAlimentRepository.findMacronutrimentsByAlimentIds(List.of(1L))).thenReturn(projections);

		ApportsNutritionnels result = nutritionService.calculApportsJournaliers(new PlanAlimentaire(), List.of(repas));

		assertEquals(new BigDecimal("20.0"), result.proteinesTot());

		assertEquals(new BigDecimal("40.0"), result.glucidesTot());

		assertEquals(new BigDecimal("10.0"), result.lipidesTot());

		// 20*4 + 40*4 + 10*9 = 330 kcal
		assertEquals(new BigDecimal("330.0"), result.energieTot());
	}

	@Test
	void calculApportsJournaliers_neChargeChaqueAlimentQuUneSeuleFois() {

		Aliment aliment = new Aliment();
		aliment.setId(1L);

		ComposantRepas composant1 = new ComposantRepas();
		composant1.setAliment(aliment);
		composant1.setQuantite(new BigDecimal("100"));

		ComposantRepas composant2 = new ComposantRepas();
		composant2.setAliment(aliment);
		composant2.setQuantite(new BigDecimal("100"));

		Repas repas = new Repas();
		repas.ajouterComposantRepas(composant1);
		repas.ajouterComposantRepas(composant2);

		when(compositionAlimentRepository.findMacronutrimentsByAlimentIds(List.of(1L)))
				.thenReturn(List.of(new MacronutrimentProjection(1L, 25000, new BigDecimal("10")),
						new MacronutrimentProjection(1L, 31000, new BigDecimal("20")),
						new MacronutrimentProjection(1L, 40000, new BigDecimal("5"))));

		nutritionService.calculApportsJournaliers(new PlanAlimentaire(), List.of(repas));

		verify(compositionAlimentRepository, times(1)).findMacronutrimentsByAlimentIds(List.of(1L));
	}

}

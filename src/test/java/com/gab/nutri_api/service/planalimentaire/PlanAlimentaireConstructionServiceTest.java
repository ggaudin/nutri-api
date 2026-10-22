package com.gab.nutri_api.service.planalimentaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gab.nutri_api.dto.planalimentaire.ComposantRepasRequest;
import com.gab.nutri_api.dto.planalimentaire.PlanAlimentaireRequest;
import com.gab.nutri_api.dto.planalimentaire.RepasRequest;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.Repas;
import com.gab.nutri_api.repository.AlimentRepository;

@ExtendWith(MockitoExtension.class)
public class PlanAlimentaireConstructionServiceTest {

	@Mock
	private AlimentRepository alimentRepository;

	@InjectMocks
	private PlanAlimentaireConstructionService constructionService;

	@Test
	void constructionPlanAlimentaire_construitLaStructureComplete() {

		Aliment aliment = new Aliment();
		aliment.setId(10L);

		when(alimentRepository.findById(10L)).thenReturn(Optional.of(aliment));

		ComposantRepasRequest composantRequest = new ComposantRepasRequest();

		composantRequest.setNom("Poulet");
		composantRequest.setQuantite(new BigDecimal("150"));
		composantRequest.setAlimentId(10L);

		RepasRequest repasRequest = new RepasRequest();
		repasRequest.setNom("Déjeuner");
		repasRequest.setRang(1);
		repasRequest.setComposantsRepas(java.util.List.of(composantRequest));

		PlanAlimentaireRequest request = new PlanAlimentaireRequest();

		request.setNom("Plan test");
		request.setNotes("Notes");
		request.setRepas(java.util.List.of(repasRequest));

		PlanAlimentaire result = constructionService.constructionPlanAlimentaire(new PlanAlimentaire(), request);

		assertEquals("Plan test", result.getNom());
		assertEquals("Notes", result.getNotes());
		assertEquals(1, result.getListeRepas().size());

		Repas repas = result.getListeRepas().get(0);

		assertEquals("Déjeuner", repas.getNom());
		assertEquals(1, repas.getRang());
		assertEquals(1, repas.getComposantsRepas().size());

		assertSame(aliment, repas.getComposantsRepas().get(0).getAliment());

		assertEquals(new BigDecimal("150"), repas.getComposantsRepas().get(0).getQuantite());

		assertSame(result, repas.getPlanAlim());
	}

}

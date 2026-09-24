package com.gab.nutri_api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.AlimentRechercheResponse;
import com.gab.nutri_api.dto.AlimentResponse;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.CompositionAliment;
import com.gab.nutri_api.repository.AlimentRepository;

@Service
public class AlimentService {

	private final AlimentRepository alimentRepository;
	
	public AlimentService(AlimentRepository alimentRepository) {
		super();
		this.alimentRepository = alimentRepository;
	}

	public List<AlimentRechercheResponse> rechercheAliment(String motRecherche) {

		if (motRecherche == null || motRecherche.trim().length() < 2) {
			return List.of();
		}

		List<Aliment> aliments = alimentRepository.findByNomContainingIgnoreCase(motRecherche);

		List<AlimentRechercheResponse> listeAliments = new ArrayList<AlimentRechercheResponse>();

		for (Aliment aliment : aliments) {

			AlimentRechercheResponse alimentRechercheResponse = new AlimentRechercheResponse();

			alimentRechercheResponse.setId(aliment.getId());
			alimentRechercheResponse.setNom(aliment.getNom());

			listeAliments.add(alimentRechercheResponse);
		}

		return listeAliments;

	}

	public AlimentResponse getAlimentById(Long alimentId) {

		Aliment aliment = alimentRepository.findById(alimentId)
				.orElseThrow(() -> new RuntimeException("Aliment non trouvé pour l'id : " + alimentId));

		AlimentResponse alimentResponse = alimentToResponse(aliment);

		return alimentResponse;

	}

	public AlimentResponse alimentToResponse(Aliment aliment) {

		AlimentResponse alimentResponse = new AlimentResponse();

		alimentResponse.setId(aliment.getId());
		alimentResponse.setNom(aliment.getNom());

		List<CompositionAliment> compositions = aliment.getCompo();

		for (CompositionAliment compo : compositions) {

			Integer code = compo.getConstituant().getCode();

			switch (code) {
			case 25000 -> alimentResponse.setProteines(compo.getTeneur() == null ? BigDecimal.ZERO : compo.getTeneur());
			case 31000 -> alimentResponse.setGlucides(compo.getTeneur() == null ? BigDecimal.ZERO : compo.getTeneur());
			case 40000 -> alimentResponse.setLipides(compo.getTeneur() == null ? BigDecimal.ZERO : compo.getTeneur());
			}
		}

		return alimentResponse;

	}

}

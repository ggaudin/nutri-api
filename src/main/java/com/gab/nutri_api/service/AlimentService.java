package com.gab.nutri_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.dto.AlimentDTO;
import com.gab.nutri_api.dto.AlimentRechercheDTO;
import com.gab.nutri_api.model.Aliment;
import com.gab.nutri_api.model.CompositionAliment;
import com.gab.nutri_api.repository.AlimentRepository;

@Service
public class AlimentService {
	
	@Autowired
	AlimentRepository alimentRepo;
	
	public List<AlimentRechercheDTO> rechercheAliment(String motRecherche){
		
		if (motRecherche == null || motRecherche.trim().length() < 2) {
	        return List.of();
	    }
		
		List<Aliment> aliments = alimentRepo.findByNomContainingIgnoreCase(motRecherche);
		
		List<AlimentRechercheDTO> listeAliments = new ArrayList<AlimentRechercheDTO>();
		
		for (Aliment aliment : aliments) {
			
			AlimentRechercheDTO alimDTO = new AlimentRechercheDTO();
			
			alimDTO.setId(aliment.getId());
			alimDTO.setNom(aliment.getNom());
			
			listeAliments.add(alimDTO);
		}

		return listeAliments;
		
	}
	
	
	public AlimentDTO getAliment(Long id) {
		
		Aliment aliment = alimentRepo.findById(id)
			    .orElseThrow(() -> new RuntimeException("Aliment non trouvé pour l'id : " + id));
		
		AlimentDTO alimDTO = new AlimentDTO();
		
		alimDTO.setId(aliment.getId());
		alimDTO.setNom(aliment.getNom());
		
		List <CompositionAliment> compositions = aliment.getCompo();
		
		for (CompositionAliment compo : compositions) {
			
			Integer code = compo.getConstituant().getCode();

		    switch (code) {
		        case 25000 -> alimDTO.setProteines(compo.getTeneur());
		        case 31000 -> alimDTO.setGlucides(compo.getTeneur());
		        case 40000 -> alimDTO.setLipides(compo.getTeneur());
		    }
		}
		
		return alimDTO;
			
	}
	
}

package com.gab.nutri_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gab.nutri_api.dto.AlimentResponse;
import com.gab.nutri_api.dto.AlimentRechercheResponse;
import com.gab.nutri_api.service.AlimentService;

@RestController
@RequestMapping("/aliments")
public class AlimentsController {
	
	@Autowired
	private AlimentService alimentService;
	
	
	//Recherche aliment
	@GetMapping("/search")
    public List<AlimentRechercheResponse> rechercheAliment(@RequestParam String motRecherche) {
        return alimentService.rechercheAliment(motRecherche);
	}
	
	//récupère un aliment précis à partir de son Id
	@GetMapping("/{id}")
	public AlimentResponse getAlimentById(@PathVariable Long id) {
	    return alimentService.getAlimentById(id);
	}
	

//	Pour aliments personnalisés (plus tard)
//	POST /aliments
//	PUT /aliments/{id}
//	DELETE /aliments/{id}

}

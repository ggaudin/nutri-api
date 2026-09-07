package com.gab.nutri_api.dto.planalimentaire;

import java.util.ArrayList;
import java.util.List;

public class RepasResponse {
	
	private Integer id;
	private String nom;
	private Integer rang;
	private List<ComposantRepasResponse> composantsRepas = new ArrayList<>();;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Integer getRang() {
		return rang;
	}
	public void setRang(Integer rang) {
		this.rang = rang;
	}
	public List<ComposantRepasResponse> getComposantsRepas() {
		return composantsRepas;
	}
	public void setComposantsRepas(List<ComposantRepasResponse> composantsRepas) {
		this.composantsRepas = composantsRepas;
	}

}

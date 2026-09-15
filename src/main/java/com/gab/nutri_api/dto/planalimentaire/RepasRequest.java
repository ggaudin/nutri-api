package com.gab.nutri_api.dto.planalimentaire;

import java.util.ArrayList;
import java.util.List;

public class RepasRequest {
	
	private Integer id;
	private String nom;
	private Integer rang;
	private List<ComposantRepasRequest> composantsRepas = new ArrayList<>();
	
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
	public List<ComposantRepasRequest> getComposantsRepas() {
		return composantsRepas;
	}
	public void setComposantsRepas(List<ComposantRepasRequest> composantsRepas) {
		this.composantsRepas = composantsRepas;
	}
	
	
}

package com.gab.nutri_api.dto.planalimentaire;

import java.util.ArrayList;
import java.util.List;

public class PlanAlimentaireRequest {

	private Integer id;
	private String nom;
	private String notes;
	private List<RepasRequest> repas = new ArrayList<>();
	
	
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<RepasRequest> getRepas() {
		return repas;
	}

	public void setRepas(List<RepasRequest> repas) {
		this.repas = repas;
	}

}

package com.gab.nutri_api.dto;

import java.math.BigDecimal;

public class AlimentResponse {
	
	private Long id;
	private String nom;
	private BigDecimal proteines;
	private BigDecimal glucides;
	private BigDecimal lipides;
	
	//D'autres constituants pourront être ajoutés plus tard si besoin
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public BigDecimal getProteines() {
		return proteines;
	}
	public void setProteines(BigDecimal proteines) {
		this.proteines = proteines;
	}
	public BigDecimal getGlucides() {
		return glucides;
	}
	public void setGlucides(BigDecimal glucides) {
		this.glucides = glucides;
	}
	public BigDecimal getLipides() {
		return lipides;
	}
	public void setLipides(BigDecimal lipides) {
		this.lipides = lipides;
	}
		

}

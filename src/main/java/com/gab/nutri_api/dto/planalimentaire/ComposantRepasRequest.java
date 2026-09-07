package com.gab.nutri_api.dto.planalimentaire;

import java.math.BigDecimal;

public class ComposantRepasRequest {
	
	private String nom;
	private BigDecimal quantite;
	private Long alimentId;
	
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public BigDecimal getQuantite() {
		return quantite;
	}
	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}
	public Long getAlimentId() {
		return alimentId;
	}
	public void setAlimentId(Long alimentId) {
		this.alimentId = alimentId;
	}
	
	

}

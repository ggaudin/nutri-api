package com.gab.nutri_api.dto.planalimentaire;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlanAlimentaireResponse {
	
	private Integer id;
	private Integer patientId;
	private Integer dietId;
	private String nom;
	private String notes;
	private List<RepasResponse> repas = new ArrayList<>();
	private BigDecimal proteinesTot;
	private BigDecimal glucidesTot;
	private BigDecimal lipidesTot;
	private BigDecimal energieTot;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getDietId() {
		return dietId;
	}
	public void setDietId(Integer dietId) {
		this.dietId = dietId;
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
	public List<RepasResponse> getRepas() {
		return repas;
	}
	public void setRepas(List<RepasResponse> repas) {
		this.repas = repas;
	}
	public BigDecimal getProteinesTot() {
		return proteinesTot;
	}
	public void setProteinesTot(BigDecimal proteinesTot) {
		this.proteinesTot = proteinesTot;
	}
	public BigDecimal getGlucidesTot() {
		return glucidesTot;
	}
	public void setGlucidesTot(BigDecimal glucidesTot) {
		this.glucidesTot = glucidesTot;
	}
	public BigDecimal getLipidesTot() {
		return lipidesTot;
	}
	public void setLipidesTot(BigDecimal lipidesTot) {
		this.lipidesTot = lipidesTot;
	}
	public BigDecimal getEnergieTot() {
		return energieTot;
	}
	public void setEnergieTot(BigDecimal energieTot) {
		this.energieTot = energieTot;
	}

}

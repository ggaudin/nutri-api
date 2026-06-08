package com.gab.nutri_api.model;

import java.util.List;

import com.gab.nutri_api.model.enums.ScopePlan;
import com.gab.nutri_api.model.enums.TypePlan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="planalimentaire")
public class PlanAlimentaire {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private Patient patient;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dieteticien_id", nullable = false)
	private Dieteticien dieteticien;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TypePlan type = TypePlan.PERSONNALISE;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ScopePlan scope = ScopePlan.PRIVATE;
	
	@Column(length = 255)
	private String nom;
	
	@Lob
	private String notes;
	
	@OneToMany(mappedBy = "planAlim")
	private List<Repas> listeRepas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Dieteticien getDieteticien() {
		return dieteticien;
	}

	public void setDieteticien(Dieteticien dieteticien) {
		this.dieteticien = dieteticien;
	}

	public TypePlan getType() {
		return type;
	}

	public void setType(TypePlan type) {
		this.type = type;
	}

	public ScopePlan getScope() {
		return scope;
	}

	public void setScope(ScopePlan scope) {
		this.scope = scope;
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

	public List<Repas> getListeRepas() {
		return listeRepas;
	}

	public void setListeRepas(List<Repas> listeRepas) {
		this.listeRepas = listeRepas;
	}

	
}

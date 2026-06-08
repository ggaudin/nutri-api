package com.gab.nutri_api.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="dieteticien")
public class Dieteticien {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", unique = true, nullable = false)
    private Utilisateur utilisateur;
	
	@Column(nullable = false, unique = true, length = 11)
	private String rpps;
	
	@OneToMany(mappedBy = "dieteticien")
    private List<Patient> patients;
	
	@OneToMany(mappedBy = "dieteticien")
	private List<PlanAlimentaire> plansAlim;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getRpps() {
		return rpps;
	}

	public void setRpps(String rpps) {
		this.rpps = rpps;
	}

}

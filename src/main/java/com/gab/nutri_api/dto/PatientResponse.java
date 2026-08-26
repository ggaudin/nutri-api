package com.gab.nutri_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gab.nutri_api.model.enums.GenrePatient;

public class PatientResponse {
	
	private Integer id;
	private String nom;
	private String prenom;
	private String email;
	
	private BigDecimal taille;
	private BigDecimal poids;
	private BigDecimal nap;
	private LocalDate dateNaissance;
	private GenrePatient genre;
	
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
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getTaille() {
		return taille;
	}
	public void setTaille(BigDecimal taille) {
		this.taille = taille;
	}
	public BigDecimal getPoids() {
		return poids;
	}
	public void setPoids(BigDecimal poids) {
		this.poids = poids;
	}
	public BigDecimal getNap() {
		return nap;
	}
	public void setNap(BigDecimal nap) {
		this.nap = nap;
	}
	public LocalDate getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public GenrePatient getGenre() {
		return genre;
	}
	public void setGenre(GenrePatient genre) {
		this.genre = genre;
	}

	
	
}

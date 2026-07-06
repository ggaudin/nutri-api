package com.gab.nutri_api.dto.auth;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gab.nutri_api.model.enums.GenrePatient;
import com.gab.nutri_api.model.enums.RoleUtilisateur;

public class RegisterRequest {
	
	//champs communs à tous les utilisateurs
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private RoleUtilisateur role; //PATIENT ou DIETETICIEN
	
	//champs patient ou diet
	
	//Si patient
	private BigDecimal taille;
	private BigDecimal poids;
	private BigDecimal nap;
	private LocalDate dateDeNaissance;
	private GenrePatient genre;
	private Integer dieteticienId;
	
	
	//Si diet
	private String rpps;
	
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public RoleUtilisateur getRole() {
		return role;
	}
	public void setRole(RoleUtilisateur role) {
		this.role = role;
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
	public LocalDate getDateDeNaissance() {
		return dateDeNaissance;
	}
	public void setDateDeNaissance(LocalDate dateDeNaissance) {
		this.dateDeNaissance = dateDeNaissance;
	}
	public GenrePatient getGenre() {
		return genre;
	}
	public void setGenre(GenrePatient genre) {
		this.genre = genre;
	}
	public Integer getDieteticienId() {
		return dieteticienId;
	}
	public void setDieteticienId(Integer dieteticienId) {
		this.dieteticienId = dieteticienId;
	}
	public String getRpps() {
		return rpps;
	}
	public void setRpps(String rpps) {
		this.rpps = rpps;
	}	
	
}

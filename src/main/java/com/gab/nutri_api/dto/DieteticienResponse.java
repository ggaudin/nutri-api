package com.gab.nutri_api.dto;

public class DieteticienResponse {
	
	private Integer id;
	private String nom;
	private String prenom;
	private String email;
	private String rpps;
	
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
	public String getRpps() {
		return rpps;
	}
	public void setRpps(String rpps) {
		this.rpps = rpps;
	}

	
}

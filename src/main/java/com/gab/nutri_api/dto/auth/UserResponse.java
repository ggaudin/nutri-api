package com.gab.nutri_api.dto.auth;

import com.gab.nutri_api.model.enums.RoleUtilisateur;

public class UserResponse {
	
	private Integer id;
	private String nom;
	private String prenom;
	private String email;
	private RoleUtilisateur role; //PATIENT ou DIET ou ADMIN
	
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
	public RoleUtilisateur getRole() {
		return role;
	}
	public void setRole(RoleUtilisateur role) {
		this.role = role;
	}

}

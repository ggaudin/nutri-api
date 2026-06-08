package com.gab.nutri_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "constituant")
public class Constituant {
	
	@Id
	@Column(name = "const_code")
	private Integer code;
	
	@Column(name = "const_nom_fr")
	private String nom;

	public Integer getCode() {
		return code;
	}

	public String getNom() {
		return nom;
	}

}

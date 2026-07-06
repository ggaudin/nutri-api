package com.gab.nutri_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.gab.nutri_api.model.enums.GenrePatient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
    @JoinColumn(name = "utilisateur_id", unique = true, nullable = false)
    private Utilisateur utilisateur;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dieteticien_id")
    private Dieteticien dieteticien;
	
	private BigDecimal taille;
	
	private BigDecimal poids;
	
	private BigDecimal nap;
	
	@Column(name = "datedenaissance")
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	private GenrePatient genre;
	
	@OneToMany(mappedBy = "patient")
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

	public Dieteticien getDieteticien() {
		return dieteticien;
	}

	public void setDieteticien(Dieteticien dieteticien) {
		this.dieteticien = dieteticien;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate localDate) {
		this.date = localDate;
	}

	public GenrePatient getGenre() {
		return genre;
	}

	public void setGenre(GenrePatient genre) {
		this.genre = genre;
	}

}

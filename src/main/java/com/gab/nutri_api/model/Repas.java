package com.gab.nutri_api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "repas")
public class Repas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "planalimentaire_id", nullable = false)
	private PlanAlimentaire planAlim;

	@Column(length = 255)
	private String nom;

	private Integer rang;

	@OneToMany(mappedBy = "repas", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ComposantRepas> composantsRepas = new ArrayList<>();

	public void ajouterComposantRepas(ComposantRepas composantRepas) {
		composantsRepas.add(composantRepas);
		composantRepas.setRepas(this);
	}
	
	public void supprimerComposantRepas(ComposantRepas composantRepas) {
		composantsRepas.remove(composantRepas);
	}
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PlanAlimentaire getPlanAlim() {
		return planAlim;
	}

	public void setPlanAlim(PlanAlimentaire planAlim) {
		this.planAlim = planAlim;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getRang() {
		return rang;
	}

	public void setRang(Integer rang) {
		this.rang = rang;
	}

	public List<ComposantRepas> getComposantsRepas() {
		return composantsRepas;
	}

	public void setComposantsRepas(List<ComposantRepas> composantsRepas) {
		this.composantsRepas = composantsRepas;
	}

}

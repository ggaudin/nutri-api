package com.gab.nutri_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "compoalim",
	   uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"aliment_id", "const_code"}
        )
    }
)
public class CompositionAliment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "const_code", nullable = false)
	private Constituant constituant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aliment_id", nullable = false)
    private Aliment aliment;

    private BigDecimal teneur;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Constituant getConstituant() {
		return constituant;
	}

	public void setConstituant(Constituant constituant) {
		this.constituant = constituant;
	}

	public Aliment getAliment() {
		return aliment;
	}

	public void setAliment(Aliment aliment) {
		this.aliment = aliment;
	}

	public BigDecimal getTeneur() {
		return teneur;
	}

	public void setTeneur(BigDecimal teneur) {
		this.teneur = teneur;
	}

}

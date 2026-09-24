package com.gab.nutri_api.dto.planalimentaire;

import java.math.BigDecimal;

public class ApportsNutritionnels {
	
	private BigDecimal proteinesTot;
	private BigDecimal glucidesTot;
	private BigDecimal lipidesTot;
	private BigDecimal energieTot;
	
	public BigDecimal getProteinesTot() {
		return proteinesTot;
	}
	public void setProteinesTot(BigDecimal proteinesTot) {
		this.proteinesTot = proteinesTot;
	}
	public BigDecimal getGlucidesTot() {
		return glucidesTot;
	}
	public void setGlucidesTot(BigDecimal glucidesTot) {
		this.glucidesTot = glucidesTot;
	}
	public BigDecimal getLipidesTot() {
		return lipidesTot;
	}
	public void setLipidesTot(BigDecimal lipidesTot) {
		this.lipidesTot = lipidesTot;
	}
	public BigDecimal getEnergieTot() {
		return energieTot;
	}
	public void setEnergieTot(BigDecimal energieTot) {
		this.energieTot = energieTot;
	}
	
	

}

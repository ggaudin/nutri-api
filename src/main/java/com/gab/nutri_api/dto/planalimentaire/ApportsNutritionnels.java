package com.gab.nutri_api.dto.planalimentaire;

import java.math.BigDecimal;

public record ApportsNutritionnels (BigDecimal proteinesTot, BigDecimal glucidesTot, BigDecimal lipidesTot, BigDecimal energieTot) {}

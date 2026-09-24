package com.gab.nutri_api.dto;

import com.gab.nutri_api.model.Dieteticien;
import com.gab.nutri_api.model.Patient;

public record PatientDieteticien(Patient patient, Dieteticien dieteticien) {

}

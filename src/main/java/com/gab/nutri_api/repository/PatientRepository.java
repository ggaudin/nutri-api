package com.gab.nutri_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.Patient;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer>{
	
	List<Patient> findPatientByDieteticienId(Integer dietId);
	
	Optional<Patient> findByUtilisateurEmail(String email);

}

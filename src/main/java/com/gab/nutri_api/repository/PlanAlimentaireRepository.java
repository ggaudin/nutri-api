package com.gab.nutri_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.enums.TypePlan;
import com.gab.nutri_api.model.enums.VisibilitePlan;

@Repository
public interface PlanAlimentaireRepository extends CrudRepository<PlanAlimentaire, Integer>{
	
	List<PlanAlimentaire> findByPatientId(Integer patientId);
	
	List<PlanAlimentaire> findByDieteticienIdAndTypeOrVisibilite(Integer dieteticienId, TypePlan typePlan, VisibilitePlan visibilitePlan);

	@Query("""
			    SELECT p
			    FROM PlanAlimentaire p
			    LEFT JOIN FETCH p.patient
			    LEFT JOIN FETCH p.dieteticien
			    WHERE p.id = :planId
			""")
	Optional<PlanAlimentaire> findByIdWithPatientAndDieteticien(Integer planId);
}

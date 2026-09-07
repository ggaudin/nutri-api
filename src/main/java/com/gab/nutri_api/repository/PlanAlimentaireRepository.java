package com.gab.nutri_api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.PlanAlimentaire;
import com.gab.nutri_api.model.enums.TypePlan;
import com.gab.nutri_api.model.enums.VisibilitePlan;

@Repository
public interface PlanAlimentaireRepository extends CrudRepository<PlanAlimentaire, Integer>{
	
	List<PlanAlimentaire> findByPatientId(Integer patientId);
	
	List<PlanAlimentaire> findByDieteticienIdAndTypePlanOrVisibilitePlan(Integer dieteticienId, TypePlan typePlan, VisibilitePlan visibilitePlan);

}

package com.gab.nutri_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.PlanAlimentaire;

@Repository
public interface PlanAlimentaireRepository extends CrudRepository<PlanAlimentaire, Integer>{

}

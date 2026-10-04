package com.gab.nutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.Repas;

@Repository
public interface RepasRepository extends CrudRepository<Repas, Integer> {

	@Query("""
			    SELECT DISTINCT r
			    FROM Repas r
			    LEFT JOIN FETCH r.composantsRepas c
				JOIN FETCH c.aliment
			    WHERE r.planAlim.id = :planId
			""")
	List<Repas> findByPlanIdWithComposantsRepasAndAliment(Integer planId);

}
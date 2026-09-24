package com.gab.nutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.ComposantRepas;

@Repository
public interface ComposantRepasRepository extends CrudRepository<ComposantRepas, Integer> {

	@Query("""
		    SELECT c
		    FROM ComposantRepas c
		    JOIN FETCH c.aliment
		    WHERE c.repas.planAlim.id = :planAlimentaireId
		""")
		List<ComposantRepas> findComposantsWithAlimentByPlanId(Integer planAlimentaireId);

}

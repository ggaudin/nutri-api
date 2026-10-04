package com.gab.nutri_api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.dto.planalimentaire.MacronutrimentProjection;
import com.gab.nutri_api.model.CompositionAliment;

@Repository
public interface CompositionAlimentRepository extends CrudRepository<CompositionAliment, Integer> {

	@Query("""
			    SELECT new com.gab.nutri_api.dto.planalimentaire.MacronutrimentProjection(
			        c.aliment.id,
			        c.constituant.code,
			        c.teneur
			    )
			    FROM CompositionAliment c
			    WHERE c.aliment.id IN :alimentIds
			      AND c.constituant.code IN (25000, 31000, 40000)
			""")
	List<MacronutrimentProjection> findMacronutrimentsByAlimentIds(@Param("alimentIds") Collection<Long> alimentIds);

}

package com.gab.nutri_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.Aliment;

@Repository
public interface AlimentRepository extends CrudRepository<Aliment, Integer>{

}

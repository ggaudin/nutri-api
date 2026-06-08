package com.gab.nutri_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.Constituant;

@Repository
public interface ConstituantRepository extends CrudRepository<Constituant, Integer>{

}

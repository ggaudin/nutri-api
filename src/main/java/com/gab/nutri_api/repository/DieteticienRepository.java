package com.gab.nutri_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.Dieteticien;

@Repository
public interface DieteticienRepository extends CrudRepository<Dieteticien, Integer> {

}

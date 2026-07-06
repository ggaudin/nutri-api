package com.gab.nutri_api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gab.nutri_api.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer>{
	
	Optional<Utilisateur> findByEmail(String email);

}

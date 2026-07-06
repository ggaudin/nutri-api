package com.gab.nutri_api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gab.nutri_api.model.Utilisateur;
import com.gab.nutri_api.repository.UtilisateurRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UtilisateurRepository utilisateurRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Utilisateur utilisateur = utilisateurRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

		
		return new User(
				utilisateur.getEmail(),
				utilisateur.getPassword(),
				getGrantedAuthorities(utilisateur.getRole().name())
	        );
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
	}

}

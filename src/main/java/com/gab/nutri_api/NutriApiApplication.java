package com.gab.nutri_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NutriApiApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(NutriApiApplication.class, args);
	}
	
//	@Autowired
//	PasswordEncoder passwordEncoder;
//	
//	@Autowired
//	UtilisateurRepository utilisateurRepo;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
//		Utilisateur admin = new Utilisateur();
//		admin.setEmail("admin@test.com");
//		
//		String password = "admin";
//		String hashedPassword = passwordEncoder.encode(password);
//		admin.setPassword(hashedPassword);
//		
//		admin.setRole(RoleUtilisateur.ADMIN);
//		
//		utilisateurRepo.save(admin);
	}

}

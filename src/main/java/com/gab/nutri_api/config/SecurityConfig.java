package com.gab.nutri_api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gab.nutri_api.security.CustomUserDetailsService;
import com.gab.nutri_api.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())  //csrf non utile si présence d'un jwt dans le header HTTP

        	.cors(Customizer.withDefaults())
        	
        	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	
        	.exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
        	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        	        })
        	    )
        
            .authorizeHttpRequests(auth -> auth

                //url accessibles à tous
                .requestMatchers("/auth/inscription", "/auth/connexion").permitAll()

                //toutes les autres sont accessibles via authentification
                .anyRequest().authenticated()
            )
            
            .authenticationProvider(authenticationProvider())
            
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	
	 @Bean
	    AuthenticationProvider authenticationProvider() {

	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

	        provider.setPasswordEncoder(passwordEncoder());

	        return provider;
	    }
	 
	 @Bean
	 CorsConfigurationSource corsConfigurationSource() {

	     CorsConfiguration configuration = new CorsConfiguration();

	     configuration.setAllowedOrigins(List.of("http://localhost:5173"));

	     configuration.setAllowedMethods(List.of(
	             "GET",
	             "POST",
	             "PUT",
	             "DELETE",
	             "OPTIONS"
	     ));

	     configuration.setAllowedHeaders(List.of(
	             "Authorization",
	             "Content-Type"
	     ));

	     // À false pour une API JWT
	     configuration.setAllowCredentials(false);

	     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

	     source.registerCorsConfiguration("/**", configuration);

	     return source;
	 }

}

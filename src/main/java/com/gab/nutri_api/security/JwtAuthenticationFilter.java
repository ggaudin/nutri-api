package com.gab.nutri_api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	      
		String authHeader = request.getHeader("Authorization");

	        
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			
			filterChain.doFilter(request,response);
			return;
		}
		
		String jwt = authHeader.substring(7);
		
		try {

		    String username = jwtService.extractUsername(jwt);
	
		    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
		    	
		    	UserDetails user = userDetailsService.loadUserByUsername(username);
	
		        if(jwtService.isTokenValid(jwt,user)){
		        	
		        	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	
		            SecurityContextHolder.getContext().setAuthentication(auth);
		            
		            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		         }
		    }
		} catch (Exception e) {
			
			SecurityContextHolder.clearContext();

	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");

	        response.getWriter().write("""
	            {
	                "error": "Invalid or expired JWT"
	            }
	        """);

	        return;
			
		}
	    
	    filterChain.doFilter(request,response);	
	    
	}
}

package com.bankapp.bank_backend.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 *Ova klasa implementira interfejs AuthenticationEntriPoint za rukovanje
 * pokusaji neovlascenog pristupa. On je odgovoran za pocetak
 * shema autentifikacije i slanje odgovarajucceg odgovora kada 
 * neautorizovani korisnik pokušava da pristupi zasticenom resursu
 * */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	/**
	 *Ovaj metod se poziva kada korisnik bez autentifikacije pokusa da pristupi zasticenom
     * resurs. Postavlja status HTTP odgovora na 401 (Neovlasceno) i salje
     * poruka o gresci.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("Access Denied !! " + authException.getMessage());
	}

}

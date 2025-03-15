package com.bankapp.bank_backend.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bankapp.bank_backend.exception.InvalidTokenException;
import com.bankapp.bank_backend.service.TokenService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;



/**
 *JVT filter za autentifikaciju
 * 
 * Ovaj filter presrece dolazne zahteve za autentifikaciju korisnika na osnovu JVT-a
 * tokena. Prosiruje OncePerRekuestFilter da bi se osiguralo da se izvrsava jednom po
 * zahtevu.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	
	private final UserDetailsService userDetailsService;
	private final TokenService tokenService;
	
	/**
	 * Izvodi filtriranje za svaki HTTP zahtev.
	 * Koristite val kao tip bilo koje deklaracije lokalne promenljive (cak i u izjavi za svaku), a tip ce se zakljuciti iz izraza za inicijalizaciju.
	 * Tacnije, moras uraditi ovo import lombok.val
	 */
	@Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.info("User is already authenticated");

            filterChain.doFilter(request, response);
            return;
        }

        val requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!requestTokenHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token must start with 'Bearer '");

            return;
        }

        val token = requestTokenHeader.substring(7);
        String username = null;

        try {
            tokenService.validateToken(token);
            username = tokenService.getUsernameFromToken(token);

        } catch (InvalidTokenException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    e.getMessage());
            return;
        }

        val userDetails = userDetailsService.loadUserByUsername(username);
        val authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}

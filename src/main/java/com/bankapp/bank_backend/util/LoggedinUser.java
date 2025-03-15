package com.bankapp.bank_backend.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.bankapp.bank_backend.exception.NotFoundException;

import lombok.val;

public class LoggedinUser {

	/**
	 * Vraca broj naloga trenutno prijavljenog korisnika. Ako postoji
     * da nijedan korisnik nije prijavljen, izbacuje se izuzetak.
	 * @return Broj racuna trenutno prijavljenog korisnika.
	 * @throws Izbacuje NotFoundException ako korisnik nije ulogovan.
	 */
	public static String getAccountNumber() {
		val authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null) {
			throw new NotFoundException("No user is currently logged in.");
		}
		val pricipal = (User)authentication.getPrincipal();
		return pricipal.getUsername();
	}
}

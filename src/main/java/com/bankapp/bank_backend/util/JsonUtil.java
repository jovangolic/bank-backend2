package com.bankapp.bank_backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonUtil {

	public static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 *Koristim ObjectMapper za pretvaranje Java objekata u JSON (serijalizacija) i obrnuto, tj. za pretvaranje JSON-a u Java objekte (deserijalizacija).
	 *ObjectMapper je osnovni alat za rad sa JSON-om u Javi, omogućavajući jednostavno mapiranje podataka između JSON formata
	 *i Java objekata. Ova funkcionalnost je ključna za rad sa REST API-jevima,
	 *konfiguracionim datotekama, ili bilo kojim drugim scenarijem gde je JSON format prisutan.
	 */
	public static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Ova metoda pretvara objekat u JSON string
	 * @param obj - objekat koji se pretvara u JSON
	 * @return JSON string koji predstavlja taj objekat
	 */
	public static String toJson(Object obj) {
		try {
			/**
			 * Serijalizacija, tj pretvaranja Java objekta u JSON
			 */
			return objectMapper.writeValueAsString(obj);
		}
		catch(JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
}

package com.bankapp.bank_backend.dto;

/**
 *Ova klasa omogucava smanjenje sablonskog koda. Takodje, kada se kreira record klasa sa zadatim
 *parametrima, automatski se kreiraju konstruktor, getter/setter metode, equals, hash, toString metode.
 *Ova record klasa se ne moze nasledjivati, sva polja su nepromenvljiva.
 *Takodje, ova record klasa se moze smatrati kao DTO.
 *Ono sto je vazno jeste, da je record od java 16 verzije potpuno podrzan.
 */
public record LoginRequest(String identifier, String password, boolean useOtp) {

}

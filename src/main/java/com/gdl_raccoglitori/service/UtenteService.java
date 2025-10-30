package com.gdl_raccoglitori.service;

import java.util.*;

import com.gdl_raccoglitori.dto.request.UtenteRequest;
import com.gdl_raccoglitori.model.Ruolo;
import com.gdl_raccoglitori.model.Utente;

public interface UtenteService
{
    List<Utente> findAll();
    Optional<Utente> findById(Long id);
    Utente findByEmail(String email);
    Optional<Utente> findByUsername(String username);
    List<Utente> findByNome(String nome);
    List<Utente> findByCognome(String cognome);
    List<Utente> findByNomeAndCognome(String nome, String cognome);
    List<Utente> search(String term);
    Utente save(Utente utente);
    Utente updateUtente(Long id, UtenteRequest utenteDTO);
    Utente attivaUtente(Long id);
    Utente disattivaUtente(Long id);
    Utente cambiaRuolo(Long id, Ruolo nuovoRuolo);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void forgotPassword(String email);
    void resetPassword(String token, String nuovaPassword);
	Utente updatePassword(Long id, String nuovaPassword);
	Optional<Utente> findByEmailOptional(String email);
	List<Utente> findByRuolo(Ruolo ruolo);
	Utente registerSocialUser(String email, String fullName);
    Utente registerNewSocialUser(String email, String nome, String cognome, String provider, String providerId);
    Utente getCurrentAuthenticatedUser();
}
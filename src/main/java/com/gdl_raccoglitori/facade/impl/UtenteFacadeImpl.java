package com.gdl_raccoglitori.facade.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.UtenteResponse;
import com.gdl_raccoglitori.facade.UtenteFacade;
import com.gdl_raccoglitori.mapper.UtenteMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.UtenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UtenteFacadeImpl implements UtenteFacade
{
	private final UtenteService utenteService;
	private final UtenteMapper utenteMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public List<UtenteResponse> getAll()
	{
		log.debug("Facade: recupero lista di tutti gli utenti");
		return utenteService.findAll().stream()
				.map(utenteMapper::toResponse)
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public UtenteResponse getById(Long id)
	{
		log.debug("Facade: recupero utente attraverso l'id");
		Utente utente = utenteService.findById(id).orElseThrow(() -> new RuntimeException("utente non trovato con id: " + id));
		return utenteMapper.toResponse(utente);
	}
	
	@Override
	@Transactional
	public UtenteResponse create(UtenteRequest request)
	{
        log.info("Facade: Inizio creazione nuovo utente: {}", request.getUsername());
		Utente utenteDaSalvare = utenteMapper.toEntity(request);
		String rawPassword = request.getPassword();
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("La password è obbligatoria per la registrazione.");
        }
        utenteDaSalvare.setPassword(passwordEncoder.encode(rawPassword));
        utenteDaSalvare.setAttivo(true);
        utenteDaSalvare.setProvider("local");
        utenteDaSalvare.setRuolo(request.getRuolo() != null ? request.getRuolo() : Ruolo.USER);
        Utente utenteSalvato = utenteService.save(utenteDaSalvare);
        return utenteMapper.toResponse(utenteSalvato);
	}
	
	@Override
    @Transactional
    public UtenteResponse update(Long id, UtenteUpdateRequest request) 
    {
        log.info("Facade: Aggiornamento dati parziale utente ID: {}", id);
        UtenteRequest requestForService = utenteMapper.toUtenteRequest(request); 
        Utente utenteAggiornato = utenteService.updateUtente(id, requestForService);
        return utenteMapper.toResponse(utenteAggiornato);
    }

    @Override
    @Transactional
    public void deleteById(Long id) 
    {
        log.warn("Facade: Richiesta di eliminazione utente ID: {}", id);
        utenteService.deleteById(id);
    }

    @Override
    @Transactional
    public UtenteResponse attivaUtente(Long id) 
    {
        Utente utente = utenteService.attivaUtente(id);
        return utenteMapper.toResponse(utente);
    }

    @Override
    @Transactional
    public UtenteResponse disattivaUtente(Long id) 
    {
        Utente utente = utenteService.disattivaUtente(id);
        return utenteMapper.toResponse(utente);
    }

    @Override
    @Transactional
    public UtenteResponse cambiaRuolo(Long id, Ruolo nuovoRuolo) 
    {
        Utente utente = utenteService.cambiaRuolo(id, nuovoRuolo);
        return utenteMapper.toResponse(utente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtenteResponse> search(String term) 
    {
        return utenteService.search(term).stream()
                .map(utenteMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UtenteResponse> findByNomeAndCognome(String nome, String cognome) 
    {
        return utenteService.findByNomeAndCognome(nome, cognome).stream()
                .map(utenteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtenteResponse> findByRuolo(Ruolo ruolo) 
    {
        return utenteService.findByRuolo(ruolo).stream()
                .map(utenteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void forgotPassword(String email) 
    {
        log.info("Facade: Richiesta recupero password per email: {}", email);
        utenteService.forgotPassword(email);
    }

    @Override
    public void resetPassword(String token, String nuovaPassword) 
    {
        log.info("Facade: Richiesta reset password con token.");
        utenteService.resetPassword(token, nuovaPassword);
    }

    @Override
    @Transactional
    public UtenteResponse updatePassword(Long id, String nuovaPassword) 
    {
        log.info("Facade: Aggiornamento password per utente ID: {}", id);
        Utente utenteAggiornato = utenteService.updatePassword(id, nuovaPassword);
        return utenteMapper.toResponse(utenteAggiornato);
    }
}

package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.dto.request.ImpostazioniRequest;
import com.gdl_raccoglitori.model.Impostazioni;
import com.gdl_raccoglitori.model.Utente;
import com.gdl_raccoglitori.repository.ImpostazioniRepository;
import com.gdl_raccoglitori.repository.UtenteRepository;
import com.gdl_raccoglitori.service.ImpostazioniService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImpostazioniServiceImpl implements ImpostazioniService 
{

    private final ImpostazioniRepository impostazioniRepository;
    private final UtenteRepository utenteRepository;

    @Override
    @Transactional(readOnly = true)
    public Impostazioni getImpostazioniUtente(Long utenteId) 
    {
        log.debug("Recupero impostazioni per utente ID: {}", utenteId);
        
        return impostazioniRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new RuntimeException("Impostazioni non trovate per utente ID: " + utenteId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Impostazioni> getImpostazioniUtenteOptional(Long utenteId) 
    {
        log.debug("Recupero impostazioni opzionali per utente ID: {}", utenteId);
        return impostazioniRepository.findByUtenteId(utenteId);
    }

    @Override
    @Transactional
    public Impostazioni createImpostazioniDefault(Long utenteId) 
    {
        log.info("Creazione impostazioni default per utente ID: {}", utenteId);
        
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));
        
        // Verifica se già esistono impostazioni
        if (impostazioniRepository.existsByUtenteId(utenteId)) 
        {
            throw new RuntimeException("Impostazioni già esistenti per utente ID: " + utenteId);
        }
        
        Impostazioni impostazioni = Impostazioni.builder()
                .utente(utente)
                .notificheEmail(true)
                .notifichePush(true)
                .lingua("it")
                .tema("system")
                .emailRiassuntoSettimanale(false)
                .privacyProfiloPubblico(true)
                .build();
        
        return impostazioniRepository.save(impostazioni);
    }

    @Override
    @Transactional
    public Impostazioni updateImpostazioni(Long utenteId, ImpostazioniRequest request) 
    {
        log.info("Aggiornamento impostazioni per utente ID: {}", utenteId);
        
        Impostazioni impostazioni = impostazioniRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new RuntimeException("Impostazioni non trovate per utente ID: " + utenteId));
        
        // Aggiorna solo i campi forniti (patch-style)
        if (request.getNotificheEmail() != null) 
        {
            impostazioni.setNotificheEmail(request.getNotificheEmail());
        }
        
        if (request.getNotifichePush() != null) 
        {
            impostazioni.setNotifichePush(request.getNotifichePush());
        }
        
        if (request.getLingua() != null)
        {
            impostazioni.setLingua(request.getLingua());
        }
        
        if (request.getTema() != null)
        {
            impostazioni.setTema(request.getTema());
        }
        
        if (request.getEmailRiassuntoSettimanale() != null) 
        {
            impostazioni.setEmailRiassuntoSettimanale(request.getEmailRiassuntoSettimanale());
        }
        
        if (request.getPrivacyProfiloPubblico() != null)
        {
            impostazioni.setPrivacyProfiloPubblico(request.getPrivacyProfiloPubblico());
        }
        
        return impostazioniRepository.save(impostazioni);
    }

    @Override
    @Transactional
    public void deleteImpostazioni(Long utenteId) 
    {
        log.warn("Eliminazione impostazioni per utente ID: {}", utenteId);
        impostazioniRepository.deleteByUtenteId(utenteId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUtenteId(Long utenteId)
    {
        return impostazioniRepository.existsByUtenteId(utenteId);
    }
}
package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.exceptionhandler.exception.RisorsaNonTrovataException;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.VotoUtenteRepository;
import com.gdl_raccoglitori.service.VotoUtenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.YearMonth;
import java.time.format.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotoUtenteServiceImpl implements VotoUtenteService 
{
    private final VotoUtenteRepository votoUtenteRepository;
    
    @Override
    public VotoUtente findById(Long id)
    {
        log.debug("Ricerca VotoUtente con ID: {}", id);
        return votoUtenteRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Voto non trovato con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, String meseVotazione) 
    {
        String cleanedMeseVotazione = meseVotazione.replaceAll("[^0-9-]", "").trim();

        if (cleanedMeseVotazione.isEmpty() || cleanedMeseVotazione.length() != 7) {
             log.error("Stringa meseVotazione pulita non valida o incompleta: '{}'", cleanedMeseVotazione);
             throw new IllegalArgumentException("Formato data non valido: utilizzare YYYY-MM. Esempio: 2025-11");
        }
        
        try 
        {
            YearMonth.parse(cleanedMeseVotazione, DateTimeFormatter.ofPattern("yyyy-MM"));
            log.debug("Mese votazione correttamente parsato: {}", cleanedMeseVotazione);
        } 
        catch (DateTimeParseException e) 
        {
            log.error("Errore di parsing del meseVotazione: {}. Richiesto formato YYYY-MM.", cleanedMeseVotazione, e);
            throw new IllegalArgumentException("Formato data non valido: utilizzare YYYY-MM. Esempio: 2025-11", e);
        }

        log.info("Verifica voti esistenti per utente ID {} (nome: {}) nel mese {}", 
                 utente.getId(), utente.getNome(), cleanedMeseVotazione);
                 
        return votoUtenteRepository.findByUtenteAndMeseVotazione(utente, cleanedMeseVotazione); 
    }
}

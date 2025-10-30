package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.exceptionhandler.exception.RisorsaNonTrovataException;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.VotoUtenteRepository;
import com.gdl_raccoglitori.service.VotoUtenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.Optional;

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
    public Optional<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, YearMonth meseVotazione)
    {
        log.debug("Ricerca VotoUtente per utente ID {} e mese {}", utente.getId(), meseVotazione);
        return votoUtenteRepository.findByUtenteAndMeseVotazione(utente, meseVotazione);
    }
}

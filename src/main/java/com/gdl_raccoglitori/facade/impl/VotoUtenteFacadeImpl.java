package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.response.VotoUtenteResponse;
import com.gdl_raccoglitori.facade.VotoUtenteFacade;
import com.gdl_raccoglitori.mapper.VotoUtenteMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.YearMonth;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class VotoUtenteFacadeImpl implements VotoUtenteFacade
{
    private final VotoUtenteService votoUtenteService;
    private final VotoUtenteMapper votoUtenteMapper;
    private final UtenteService utenteService; 

    @Override
    public VotoUtenteResponse findById(Long id)
    {
        VotoUtente voto = votoUtenteService.findById(id);
        return votoUtenteMapper.toResponse(voto);
    }

    @Override
    public VotoUtenteResponse checkExistingVote(String meseVotazione) 
    {
        Utente utente = utenteService.getCurrentAuthenticatedUser();
        log.info("Verifica voto esistente per utente ID {} nel mese {}", utente.getId(), meseVotazione);
        
        YearMonth mese = votoUtenteMapper.stringToYearMonth(meseVotazione);
        
        Optional<VotoUtente> existingVote = votoUtenteService.findByUtenteAndMeseVotazione(utente, mese);

        return existingVote
                .map(votoUtenteMapper::toResponse)
                .orElse(null); 
    }
}

package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.response.VotoUtenteResponse;
import com.gdl_raccoglitori.facade.VotoUtenteFacade;
import com.gdl_raccoglitori.mapper.VotoUtenteMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class VotoUtenteFacadeImpl implements VotoUtenteFacade
{
    private final VotoUtenteService votoUtenteService;
    private final VotoUtenteMapper votoUtenteMapper;
    private final UtenteService utenteService; 

    @Override
    @Transactional(readOnly = true)
    public VotoUtenteResponse findById(Long id)
    {
        VotoUtente voto = votoUtenteService.findById(id);
        return votoUtenteMapper.toResponse(voto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VotoUtenteResponse> checkExistingVote(String meseVotazione) 
    {
        Utente utente = utenteService.getCurrentAuthenticatedUser();
        log.info("Verifica voti esistenti per utente ID {} nel mese {}", utente.getId(), meseVotazione);
        
        List<VotoUtente> existingVotes = votoUtenteService.findByUtenteAndMeseVotazione(utente, meseVotazione);

        return existingVotes.stream()
                .map(votoUtenteMapper::toResponse)
                .collect(Collectors.toList()); 
    }
}

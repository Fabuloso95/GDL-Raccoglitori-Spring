package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.facade.PropostaVotoFacade;
import com.gdl_raccoglitori.mapper.*;
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
public class PropostaVotoFacadeImpl implements PropostaVotoFacade 
{
    private final PropostaVotoService propostaVotoService;
    private final PropostaVotoMapper propostaVotoMapper;
    private final VotoUtenteMapper votoUtenteMapper;
    private final UtenteService utenteService; 

    @Override
    public PropostaVotoResponse createProposta(PropostaVotoRequest request) 
    {
        PropostaVoto proposta = propostaVotoService.createProposta(request);
        
        return propostaVotoMapper.toResponse(proposta);
    }

    @Override
    @Transactional
    public VotoUtenteResponse voteForProposta(VotoUtenteRequest request) 
    {
        Utente utente = utenteService.getCurrentAuthenticatedUser(); 

        log.info("Tentativo di voto dell'utente ID {} per la proposta ID {} nel mese {}", 
                 utente.getId(), request.getPropostaVotoId(), request.getMeseVotazione());
        
        VotoUtente voto = propostaVotoService.registeVoto(
            request.getPropostaVotoId(), 
            utente, 
            request.getMeseVotazione()
        );
        
        return votoUtenteMapper.toResponse(voto);
    }
    
    @Override
    @Transactional
    public PropostaVotoResponse updateProposta(Long id, PropostaVotoRequest request) 
    {
        Utente utenteCorrente = utenteService.getCurrentAuthenticatedUser(); 
        
        PropostaVoto propostaAggiornata = propostaVotoService.updateProposta(id, request, utenteCorrente);
        
        log.info("Facade: Proposta ID {} aggiornata da utente ID {}", id, utenteCorrente.getId());
        return propostaVotoMapper.toResponse(propostaAggiornata);
    }
    
    @Override
    @Transactional
    public void deleteProposta(Long id) 
    {
        propostaVotoService.deleteProposta(id);
        log.warn("Facade: Proposta ID {} eliminata.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropostaVotoResponse> getProposteByMese(String meseVotazione) 
    {
        List<PropostaVoto> proposte = propostaVotoService.findByMeseVotazione(meseVotazione);

        return proposte.stream()
                .map(propostaVotoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PropostaVotoResponse getWinnerProposta(String meseVotazione) 
    {
        PropostaVoto winner = propostaVotoService.findWinnerProposta(meseVotazione);
        
        return propostaVotoMapper.toResponse(winner);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PropostaVotoResponse findById(Long id) 
    {
        PropostaVoto proposta = propostaVotoService.findById(id);
        return propostaVotoMapper.toResponse(proposta);
    }
}

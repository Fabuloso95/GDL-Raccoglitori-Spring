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
import java.time.YearMonth;
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
    private static final String DATE_FORMAT = "yyyy-MM";

    @Override
    public PropostaVotoResponse createProposta(PropostaVotoRequest request) 
    {
        PropostaVoto proposta = propostaVotoService.createProposta(request);
        
        return propostaVotoMapper.toResponse(proposta);
    }

    @Override
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
    public List<PropostaVotoResponse> getProposteByMese(String meseVotazione) 
    {
        YearMonth mese = propostaVotoMapper.stringToYearMonth(meseVotazione);

        List<PropostaVoto> proposte = propostaVotoService.findByMeseVotazione(mese);

        return proposte.stream()
                .map(propostaVotoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PropostaVotoResponse getWinnerProposta(String meseVotazione) 
    {
        YearMonth mese = propostaVotoMapper.stringToYearMonth(meseVotazione);
        
        PropostaVoto winner = propostaVotoService.findWinnerProposta(mese);
        
        return propostaVotoMapper.toResponse(winner);
    }
    
    @Override
    public PropostaVotoResponse findById(Long id) 
    {
        PropostaVoto proposta = propostaVotoService.findById(id);
        return propostaVotoMapper.toResponse(proposta);
    }
}

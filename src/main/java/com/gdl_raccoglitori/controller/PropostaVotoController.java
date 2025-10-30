package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.facade.PropostaVotoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PropostaVotoController 
{
    private final PropostaVotoFacade propostaVotoFacade;

    @PostMapping("/proposte")
    public ResponseEntity<PropostaVotoResponse> createProposta(@Valid @RequestBody PropostaVotoRequest request) 
    {
        log.info("Ricevuta richiesta per creare una nuova proposta di voto.");
        PropostaVotoResponse response = propostaVotoFacade.createProposta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/voti")
    public ResponseEntity<VotoUtenteResponse> voteForProposta(@Valid @RequestBody VotoUtenteRequest request) 
    {
        log.info("Ricevuta richiesta di voto per proposta ID: {} nel mese {}", 
                 request.getPropostaVotoId(), request.getMeseVotazione());
                 
        VotoUtenteResponse response = propostaVotoFacade.voteForProposta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/proposte/mese/{meseVotazione}")
    public ResponseEntity<List<PropostaVotoResponse>> getProposteByMese(@PathVariable String meseVotazione) 
    {
        log.info("Richiesta proposte per il mese: {}", meseVotazione);
        List<PropostaVotoResponse> response = propostaVotoFacade.getProposteByMese(meseVotazione);
        
        if (response.isEmpty()) 
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/proposte/vincitore/{meseVotazione}")
    public ResponseEntity<PropostaVotoResponse> getWinnerProposta(@PathVariable String meseVotazione) 
    {
        log.info("Richiesta vincitore per il mese: {}", meseVotazione);
        try 
        {
            PropostaVotoResponse winner = propostaVotoFacade.getWinnerProposta(meseVotazione);
            return ResponseEntity.ok(winner);
        } 
        catch (RuntimeException e) 
        {
            log.warn("Nessun vincitore trovato per il mese {}: {}", meseVotazione, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/proposte/{id}")
    public ResponseEntity<PropostaVotoResponse> findById(@PathVariable Long id) 
    {
        log.info("Richiesta proposta con ID: {}", id);
        try 
        {
            PropostaVotoResponse proposta = propostaVotoFacade.findById(id);
            return ResponseEntity.ok(proposta);
        } 
        catch (RuntimeException e)
        {
            log.warn("Proposta non trovata con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

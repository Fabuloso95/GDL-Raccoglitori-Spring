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
@RequestMapping("/api/proposte")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PropostaVotoController 
{
    private final PropostaVotoFacade propostaVotoFacade;

    @PostMapping
    public ResponseEntity<PropostaVotoResponse> createProposta(@Valid @RequestBody PropostaVotoRequest request) 
    {
        log.info("Ricevuta richiesta per creare una nuova proposta di voto.");
        PropostaVotoResponse response = propostaVotoFacade.createProposta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<PropostaVotoResponse> updateProposta(@PathVariable Long id, @Valid @RequestBody PropostaVotoRequest request)
    {
        log.info("Ricevuta richiesta di aggiornamento per proposta ID: {}", id);
        PropostaVotoResponse response = propostaVotoFacade.updateProposta(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProposta(@PathVariable Long id) 
    {
        log.warn("Ricevuta richiesta di eliminazione per proposta ID: {}", id);
        propostaVotoFacade.deleteProposta(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/voti") 
    public ResponseEntity<VotoUtenteResponse> voteForProposta(@Valid @RequestBody VotoUtenteRequest request) 
    {
        log.info("Ricevuta richiesta di voto per proposta ID: {} nel mese {}", 
                 request.getPropostaVotoId(), request.getMeseVotazione());
                 
        VotoUtenteResponse response = propostaVotoFacade.voteForProposta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/mese/{meseVotazione}")
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
    
    @GetMapping("/vincitore/{meseVotazione}")
    public ResponseEntity<PropostaVotoResponse> getWinnerProposta(@PathVariable String meseVotazione) 
    {
        log.info("Richiesta vincitore per il mese: {}", meseVotazione);
        PropostaVotoResponse winner = propostaVotoFacade.getWinnerProposta(meseVotazione);
        return ResponseEntity.ok(winner);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PropostaVotoResponse> findById(@PathVariable Long id) 
    {
        log.info("Richiesta proposta con ID: {}", id);
        PropostaVotoResponse proposta = propostaVotoFacade.findById(id);
        return ResponseEntity.ok(proposta);
    }
}

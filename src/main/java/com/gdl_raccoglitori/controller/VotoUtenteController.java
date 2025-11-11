package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.response.VotoUtenteResponse;
import com.gdl_raccoglitori.exceptionhandler.exception.RisorsaNonTrovataException;
import com.gdl_raccoglitori.facade.VotoUtenteFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voti")
@RequiredArgsConstructor
@Slf4j
public class VotoUtenteController 
{
    private final VotoUtenteFacade votoUtenteFacade;

    @GetMapping("/{id}")
    public ResponseEntity<VotoUtenteResponse> findById(@PathVariable Long id) 
    {
        log.info("Richiesta VotoUtente con ID: {}", id);
        
        try
        {
             VotoUtenteResponse response = votoUtenteFacade.findById(id);
             return ResponseEntity.ok(response);
        } 
        catch (RisorsaNonTrovataException e) 
        {
            log.warn("Voto non trovato con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/check/mese/{meseVotazione}")
    public ResponseEntity<List<VotoUtenteResponse>> checkExistingVote(@PathVariable String meseVotazione) 
    {
        log.info("Verifica voto esistente per l'utente autenticato nel mese: {}", meseVotazione);
        
        List<VotoUtenteResponse> existingVote = votoUtenteFacade.checkExistingVote(meseVotazione);
        
        if (existingVote == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        }
        
        return ResponseEntity.ok(existingVote);
    }
}

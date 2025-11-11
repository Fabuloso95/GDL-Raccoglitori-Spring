package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.ImpostazioniRequest;
import com.gdl_raccoglitori.dto.response.ImpostazioniResponse;
import com.gdl_raccoglitori.facade.ImpostazioniFacade;
import com.gdl_raccoglitori.model.Utente;
import com.gdl_raccoglitori.service.UtenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/impostazioni")
@Slf4j
@RequiredArgsConstructor
public class ImpostazioniController
{
    private final ImpostazioniFacade impostazioniFacade;
    private final UtenteService utenteService;

    @GetMapping("/utente/{utenteId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ImpostazioniResponse> getImpostazioniUtente(@PathVariable Long utenteId) 
    {
        log.info("Controller: Richiesta impostazioni per utente ID: {}", utenteId);
        
        // Verifica che l'utente autenticato possa accedere solo alle proprie impostazioni
        Utente currentUser = utenteService.getCurrentAuthenticatedUser();
        if (!currentUser.getId().equals(utenteId) && !currentUser.getRuolo().name().equals("ADMIN"))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ImpostazioniResponse response = impostazioniFacade.getImpostazioniUtente(utenteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ImpostazioniResponse> getMyImpostazioni() 
    {
        Utente currentUser = utenteService.getCurrentAuthenticatedUser();
        log.info("Controller: Richiesta impostazioni per utente corrente ID: {}", currentUser.getId());
        
        ImpostazioniResponse response = impostazioniFacade.getImpostazioniUtente(currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/utente/{utenteId}/default")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ImpostazioniResponse> createImpostazioniDefault(@PathVariable Long utenteId) 
    {
        log.info("Controller: Creazione impostazioni default per utente ID: {}", utenteId);
        
        // Verifica autorizzazione
        Utente currentUser = utenteService.getCurrentAuthenticatedUser();
        if (!currentUser.getId().equals(utenteId) && !currentUser.getRuolo().name().equals("ADMIN")) 
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ImpostazioniResponse response = impostazioniFacade.createImpostazioniDefault(utenteId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/utente/{utenteId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ImpostazioniResponse> updateImpostazioni(
            @PathVariable Long utenteId,
            @Valid @RequestBody ImpostazioniRequest request) 
    {
        log.info("Controller: Aggiornamento impostazioni per utente ID: {}", utenteId);
        
        // Verifica autorizzazione
        Utente currentUser = utenteService.getCurrentAuthenticatedUser();
        if (!currentUser.getId().equals(utenteId) && !currentUser.getRuolo().name().equals("ADMIN"))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ImpostazioniResponse response = impostazioniFacade.updateImpostazioni(utenteId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/utente/{utenteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImpostazioni(@PathVariable Long utenteId) 
    {
        log.warn("Controller: Eliminazione impostazioni per utente ID: {}", utenteId);
        impostazioniFacade.deleteImpostazioni(utenteId);
        return ResponseEntity.noContent().build();
    }
}
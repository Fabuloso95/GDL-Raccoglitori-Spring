package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.facade.LetturaCorrenteFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/letture") 
@RequiredArgsConstructor
@Slf4j
public class LetturaCorrenteController 
{
    private final LetturaCorrenteFacade letturaCorrenteFacade;

    @PostMapping
    public ResponseEntity<LetturaCorrenteResponse> startReading(@Valid @RequestBody LetturaCorrenteRequest request) 
    {
        log.info("Controller: Richiesta di inizio lettura per il libro ID {}", request.getLibroId());
        LetturaCorrenteResponse response = letturaCorrenteFacade.startNewReading(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<LetturaCorrenteResponse> updateProgress(
            @PathVariable Long id, 
            @Valid @RequestBody LetturaCorrenteUpdateRequest request) 
    {
        log.info("Controller: Richiesta aggiornamento progresso per lettura ID: {}", id);
        LetturaCorrenteResponse response = letturaCorrenteFacade.updateReadingProgress(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<LetturaCorrenteResponse> completeReading(@PathVariable Long id) 
    {
        log.info("Controller: Richiesta completamento lettura ID: {}", id);
        LetturaCorrenteResponse response = letturaCorrenteFacade.completeReading(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<LetturaCorrenteResponse>> getMyReadings() 
    {
        log.debug("Controller: Richiesta letture dell'utente autenticato.");
        List<LetturaCorrenteResponse> lettureList = letturaCorrenteFacade.findMyReadings();
        return ResponseEntity.ok(lettureList);
    }
    
    @GetMapping("/libro/{libroId}/progressi")
    public ResponseEntity<List<LetturaCorrenteProgressResponse>> getBookProgressOverview(@PathVariable Long libroId) 
    {
        log.debug("Controller: Richiesta overview progressi per libro ID: {}", libroId);
        List<LetturaCorrenteProgressResponse> progressiList = letturaCorrenteFacade.getBookProgressOverview(libroId);
        return ResponseEntity.ok(progressiList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LetturaCorrenteResponse> getReadingById(@PathVariable Long id) 
    {
        log.debug("Controller: Ricevuta richiesta per lettura ID: {}", id);
        LetturaCorrenteResponse lettura = letturaCorrenteFacade.findById(id);
        return ResponseEntity.ok(lettura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(@PathVariable Long id) 
    {
        log.warn("Controller: Ricevuta richiesta di eliminazione per lettura ID: {}", id);
        letturaCorrenteFacade.deleteReading(id);
        return ResponseEntity.noContent().build();
    }
}

package com.gdl_raccoglitori.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.dto.response.CuriositaResponse;
import com.gdl_raccoglitori.facade.CuriositaFacade;
import java.util.List;

@RestController
@RequestMapping("/api/curiosita") 
@RequiredArgsConstructor
@Slf4j
public class CuriositaController 
{
    private final CuriositaFacade curiositaFacade;

    @PostMapping
    public ResponseEntity<CuriositaResponse> createCuriosita(@Valid @RequestBody CuriositaRequest request) 
    {
        log.info("Controller: Ricevuta richiesta di creazione curiosità per il libro ID {}", request.getLibroId());
        CuriositaResponse response = curiositaFacade.createCuriosita(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuriositaResponse> getCuriositaById(@PathVariable Long id) 
    {
        log.debug("Controller: Ricevuta richiesta per curiosità ID: {}", id);
        CuriositaResponse curiosita = curiositaFacade.findById(id);
        return ResponseEntity.ok(curiosita);
    }
    
    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<CuriositaResponse>> getCuriositaByLibro(@PathVariable Long libroId) 
    {
        log.debug("Controller: Richiesta tutte le curiosità per libro ID: {}", libroId);
        List<CuriositaResponse> curiositaList = curiositaFacade.findByLibroId(libroId);
        return ResponseEntity.ok(curiositaList);
    }

    @GetMapping("/libro/{libroId}/pagina/{paginaRiferimento}")
    public ResponseEntity<List<CuriositaResponse>> getCuriositaByLibroAndPagina(@PathVariable Long libroId, @PathVariable Integer paginaRiferimento) 
    {
        log.debug("Controller: Richiesta curiosità per libro ID {} e pagina {}", libroId, paginaRiferimento);
        List<CuriositaResponse> curiositaList = curiositaFacade.findByLibroAndPagina(libroId, paginaRiferimento);
        return ResponseEntity.ok(curiositaList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuriositaResponse> updateCuriosita(@PathVariable Long id, @Valid @RequestBody CuriositaRequest request) 
    {
        log.info("Controller: Ricevuta richiesta di aggiornamento per curiosità ID: {}", id);
        CuriositaResponse response = curiositaFacade.updateCuriosita(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuriosita(@PathVariable Long id) 
    {
        log.warn("Controller: Ricevuta richiesta di eliminazione per curiosità ID: {}", id);
        curiositaFacade.deleteCuriosita(id);
        return ResponseEntity.noContent().build();
    }
}
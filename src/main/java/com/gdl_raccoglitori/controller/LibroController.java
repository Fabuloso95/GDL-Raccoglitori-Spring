package com.gdl_raccoglitori.controller;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.gdl_raccoglitori.dto.request.LibroRequest;
import com.gdl_raccoglitori.dto.response.LibroResponse;
import com.gdl_raccoglitori.facade.LibroFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/libri")
@Slf4j
@RequiredArgsConstructor
public class LibroController 
{
    private final LibroFacade libroFacade;

    @PostMapping
    public ResponseEntity<LibroResponse> creaLibro(@Valid @RequestBody LibroRequest request) 
    {
        log.info("Controller: Richiesta di creazione nuovo libro con titolo: {}", request.getTitolo());
        LibroResponse response = libroFacade.creaLibro(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> getLibroById(@PathVariable Long id) 
    {
        log.info("Controller: Richiesta recupero libro con ID: {}", id);
        LibroResponse response = libroFacade.getLibroById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LibroResponse>> getAllLibriOrSearch(
            @RequestParam(required = false) String searchTerm) 
    {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) 
        {
            log.info("Controller: Richiesta ricerca libri per termine: {}", searchTerm);
            List<LibroResponse> results = libroFacade.cercaLibri(searchTerm);
            return ResponseEntity.ok(results);
        } 
        else 
        {
            log.info("Controller: Richiesta recupero di tutti i libri.");
            List<LibroResponse> libri = libroFacade.getAllLibri();
            return ResponseEntity.ok(libri);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> aggiornaLibro(
            @PathVariable Long id, 
            @Valid @RequestBody LibroRequest request) 
    {
        log.info("Controller: Richiesta aggiornamento libro ID: {}", id);
        LibroResponse response = libroFacade.aggiornaLibro(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaLibro(@PathVariable Long id) 
    {
        log.warn("Controller: Richiesta eliminazione libro ID: {}", id);
        libroFacade.eliminaLibro(id);
        return ResponseEntity.noContent().build();
    }
}

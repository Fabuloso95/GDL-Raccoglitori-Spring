package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.EventoRequestDTO;
import com.gdl_raccoglitori.dto.response.EventoResponseDTO;
import com.gdl_raccoglitori.facade.EventoFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventoController 
{
    private final EventoFacade eventoFacade;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventoResponseDTO> creaEvento(
            @RequestBody EventoRequestDTO eventoRequestDTO,
            @AuthenticationPrincipal String username) 
    {
        EventoResponseDTO response = eventoFacade.creaEvento(eventoRequestDTO, username);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> getEventi(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) 
    {
        List<EventoResponseDTO> eventi = eventoFacade.getEventiNelPeriodo(inizio, fine);
        return ResponseEntity.ok(eventi);
    }
    
    @GetMapping("/prossima-votazione")
    public ResponseEntity<EventoResponseDTO> getProssimaVotazione() 
    {
        EventoResponseDTO votazione = eventoFacade.getProssimaVotazione();
        return votazione != null ? ResponseEntity.ok(votazione) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/prossima-discussione")
    public ResponseEntity<EventoResponseDTO> getProssimaDiscussione() 
    {
        EventoResponseDTO discussione = eventoFacade.getProssimaDiscussione();
        return discussione != null ? ResponseEntity.ok(discussione) : ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> getEventoById(@PathVariable Long id) 
    {
        EventoResponseDTO evento = eventoFacade.getEventoById(id);
        return ResponseEntity.ok(evento);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventoResponseDTO> aggiornaEvento(
            @PathVariable Long id,
            @RequestBody EventoRequestDTO eventoRequestDTO) 
    {
        EventoResponseDTO eventoAggiornato = eventoFacade.aggiornaEvento(id, eventoRequestDTO);
        return ResponseEntity.ok(eventoAggiornato);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminaEvento(@PathVariable Long id) 
    {
        eventoFacade.eliminaEvento(id);
        return ResponseEntity.noContent().build();
    }
}
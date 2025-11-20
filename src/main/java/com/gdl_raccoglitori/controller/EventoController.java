package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.EventoRequestDTO;
import com.gdl_raccoglitori.dto.response.EventoResponseDTO;
import com.gdl_raccoglitori.facade.EventoFacade;
import com.gdl_raccoglitori.security.CustomUserDetails;
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
            @AuthenticationPrincipal Object principal)
    {
        String username = extractUsernameFromPrincipal(principal);
        EventoResponseDTO response = eventoFacade.creaEvento(eventoRequestDTO, username);
        return ResponseEntity.ok(response);
    }

    private String extractUsernameFromPrincipal(Object principal) 
    {
        if (principal instanceof CustomUserDetails) 
        {
            return ((CustomUserDetails) principal).getUsername();
        } 
        else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) 
        {
            org.springframework.security.oauth2.core.user.OAuth2User oauth2User = 
                (org.springframework.security.oauth2.core.user.OAuth2User) principal;
            return oauth2User.getAttribute("email");
        } 
        else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) 
        {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } 
        else if (principal instanceof String) 
        {
            return (String) principal;
        } 
        else 
        {
            throw new RuntimeException("Tipo di principal non supportato: " + principal.getClass().getName());
        }
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
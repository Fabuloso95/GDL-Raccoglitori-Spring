package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.request.EventoRequestDTO;
import com.gdl_raccoglitori.dto.response.EventoResponseDTO;
import com.gdl_raccoglitori.facade.EventoFacade;
import com.gdl_raccoglitori.mapper.EventoMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventoFacadeImpl implements EventoFacade 
{
    
    private final EventoService eventoService;
    private final UtenteService utenteService;
    private final EventoMapper eventoMapper;
    
    @Override
    public EventoResponseDTO creaEvento(EventoRequestDTO eventoRequestDTO, String usernameCreatore) 
    {
        log.info("Creazione evento per utente: {}", usernameCreatore);
        
        Utente creatore = utenteService.findByUsername(usernameCreatore)
            .orElseThrow(() -> new RuntimeException("Utente non trovato: " + usernameCreatore));
        
        Evento evento = eventoMapper.toEntity(eventoRequestDTO);
        Evento eventoSalvato = eventoService.creaEvento(evento, creatore);
        
        return eventoMapper.toResponseDTO(eventoSalvato);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EventoResponseDTO> getEventiNelPeriodo(LocalDateTime inizio, LocalDateTime fine) 
    {
        log.info("Recupero eventi nel periodo: {} - {}", inizio, fine);
        
        return eventoService.getEventiNelPeriodo(inizio, fine)
            .stream()
            .map(eventoMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public EventoResponseDTO getProssimaVotazione() 
    {
        return eventoService.getProssimaVotazione()
            .map(eventoMapper::toResponseDTO)
            .orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EventoResponseDTO getProssimaDiscussione() 
    {
        return eventoService.getProssimaDiscussione()
            .map(eventoMapper::toResponseDTO)
            .orElse(null);
    }
    
    @Override
    public void eliminaEvento(Long id) 
    {
        eventoService.eliminaEvento(id);
    }
    
    @Override
    public EventoResponseDTO aggiornaEvento(Long id, EventoRequestDTO eventoRequestDTO) 
    {
        Evento eventoEsistente = eventoService.getEventoById(id)
            .orElseThrow(() -> new RuntimeException("Evento non trovato con ID: " + id));
        
        eventoEsistente.setTitolo(eventoRequestDTO.getTitolo());
        eventoEsistente.setDescrizione(eventoRequestDTO.getDescrizione());
        eventoEsistente.setDataInizio(eventoRequestDTO.getDataInizio());
        eventoEsistente.setDataFine(eventoRequestDTO.getDataFine());
        eventoEsistente.setTipoEvento(eventoRequestDTO.getTipoEvento());
        
        Evento eventoAggiornato = eventoService.aggiornaEvento(eventoEsistente);
        return eventoMapper.toResponseDTO(eventoAggiornato);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EventoResponseDTO getEventoById(Long id) 
    {
        return eventoService.getEventoById(id)
            .map(eventoMapper::toResponseDTO)
            .orElseThrow(() -> new RuntimeException("Evento non trovato con ID: " + id));
    }
}
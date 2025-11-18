package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.model.Evento;
import com.gdl_raccoglitori.model.TipoEvento;
import com.gdl_raccoglitori.model.Utente;
import com.gdl_raccoglitori.repository.EventoRepository;
import com.gdl_raccoglitori.service.EventoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventoServiceImpl implements EventoService 
{
    
    private final EventoRepository eventoRepository;
    
    @Override
    public Evento creaEvento(Evento evento, Utente creatore) 
    {
        log.info("Creazione evento: {} per utente: {}", evento.getTitolo(), creatore.getUsername());
        
        if (evento.getDataInizio().isAfter(evento.getDataFine())) 
        {
            throw new IllegalArgumentException("La data di inizio non può essere dopo la data di fine");
        }
        
        if (eventoRepository.existsByTipoEventoAndPeriodo(
            evento.getTipoEvento(), 
            evento.getDataInizio(), 
            evento.getDataFine())) 
        {
            throw new IllegalArgumentException("Esiste già un evento di questo tipo nel periodo specificato");
        }
        
        evento.setCreatoDa(creatore);
        return eventoRepository.save(evento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evento> getEventiNelPeriodo(LocalDateTime inizio, LocalDateTime fine) 
    {
        log.info("Recupero eventi nel periodo: {} - {}", inizio, fine);
        return eventoRepository.findByDataInizioBetweenOrderByDataInizioAsc(inizio, fine);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> getProssimaVotazione() 
    {
        log.info("Recupero prossima votazione");
        List<Evento> votazioni = eventoRepository.findProssimaVotazione(LocalDateTime.now());
        return votazioni.isEmpty() ? Optional.empty() : Optional.of(votazioni.get(0));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> getProssimaDiscussione() 
    {
        log.info("Recupero prossima discussione");
        List<Evento> discussioni = eventoRepository.findProssimaDiscussione(LocalDateTime.now());
        return discussioni.isEmpty() ? Optional.empty() : Optional.of(discussioni.get(0));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evento> getEventiFuturi() 
    {
        log.info("Recupero eventi futuri");
        return eventoRepository.findByDataInizioAfterOrderByDataInizioAsc(LocalDateTime.now());
    }
    
    @Override
    public void eliminaEvento(Long id) 
    {
        log.info("Eliminazione evento con ID: {}", id);
        if (!eventoRepository.existsById(id)) 
        {
            throw new RuntimeException("Evento non trovato con ID: " + id);
        }
        eventoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> getEventoById(Long id) 
    {
        log.info("Recupero evento con ID: {}", id);
        return eventoRepository.findById(id);
    }
    
    @Override
    public Evento aggiornaEvento(Evento evento) 
    {
        log.info("Aggiornamento evento con ID: {}", evento.getId());
        
        if (!eventoRepository.existsById(evento.getId())) 
        {
            throw new RuntimeException("Evento non trovato con ID: " + evento.getId());
        }
        
        if (evento.getDataInizio().isAfter(evento.getDataFine()))
        {
            throw new IllegalArgumentException("La data di inizio non può essere dopo la data di fine");
        }
        
        return eventoRepository.save(evento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evento> getEventiByTipo(TipoEvento tipoEvento) 
    {
        log.info("Recupero eventi per tipo: {}", tipoEvento);
        return eventoRepository.findByTipoEventoOrderByDataInizioAsc(tipoEvento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByTipoAndPeriodo(TipoEvento tipo, LocalDateTime inizio, LocalDateTime fine) 
    {
        return eventoRepository.existsByTipoEventoAndPeriodo(tipo, inizio, fine);
    }
}
package com.gdl_raccoglitori.service;

import com.gdl_raccoglitori.model.*;
import java.time.LocalDateTime;
import java.util.*;

public interface EventoService
{
    Evento creaEvento(Evento evento, Utente creatore);
    List<Evento> getEventiNelPeriodo(LocalDateTime inizio, LocalDateTime fine);
    Optional<Evento> getProssimaVotazione();
    Optional<Evento> getProssimaDiscussione();
    List<Evento> getEventiFuturi();
    void eliminaEvento(Long id);
    Optional<Evento> getEventoById(Long id);
    Evento aggiornaEvento(Evento evento);
    List<Evento> getEventiByTipo(TipoEvento tipoEvento);
    boolean existsByTipoAndPeriodo(TipoEvento tipo, LocalDateTime inizio, LocalDateTime fine);
}
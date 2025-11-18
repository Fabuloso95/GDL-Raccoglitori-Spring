package com.gdl_raccoglitori.repository;

import com.gdl_raccoglitori.model.Evento;
import com.gdl_raccoglitori.model.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> 
{
    List<Evento> findByDataInizioBetweenOrderByDataInizioAsc(
        LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Evento e WHERE e.tipoEvento = :tipo AND e.dataInizio >= :data ORDER BY e.dataInizio ASC")
    List<Evento> findProssimiEventiByTipo(@Param("tipo") TipoEvento tipo, @Param("data") LocalDateTime data);
    
    List<Evento> findByCreatoDaIdOrderByDataInizioDesc(Long userId);
    
    @Query("SELECT e FROM Evento e WHERE e.tipoEvento = 'VOTAZIONE' AND e.dataInizio > :now ORDER BY e.dataInizio ASC")
    List<Evento> findProssimaVotazione(@Param("now") LocalDateTime now);
    
    @Query("SELECT e FROM Evento e WHERE e.tipoEvento = 'DISCUSSIONE' AND e.dataInizio > :now ORDER BY e.dataInizio ASC")
    List<Evento> findProssimaDiscussione(@Param("now") LocalDateTime now);
    
    List<Evento> findByDataInizioAfterOrderByDataInizioAsc(LocalDateTime data);
    
    List<Evento> findByTipoEventoOrderByDataInizioAsc(TipoEvento tipoEvento);
    
    @Query("SELECT COUNT(e) > 0 FROM Evento e WHERE e.tipoEvento = :tipoEvento AND " +
           "((e.dataInizio BETWEEN :inizio AND :fine) OR (e.dataFine BETWEEN :inizio AND :fine) OR " +
           "(e.dataInizio <= :inizio AND e.dataFine >= :fine))")
    boolean existsByTipoEventoAndPeriodo(@Param("tipoEvento") TipoEvento tipoEvento, 
                                        @Param("inizio") LocalDateTime inizio, 
                                        @Param("fine") LocalDateTime fine);
}
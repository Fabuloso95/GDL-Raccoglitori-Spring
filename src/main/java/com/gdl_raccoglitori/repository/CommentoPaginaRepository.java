package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.gdl_raccoglitori.model.*;

public interface CommentoPaginaRepository extends JpaRepository<CommentoPagina, Long>
{
	Optional<CommentoPagina> findById(Long id);
	List<CommentoPagina> findByUtente(Utente utente);;
	List<CommentoPagina> findByLetturaCorrenteAndPaginaRiferimentoOrderByDataCreazioneAsc(LetturaCorrente letturaCorrente, Integer paginaRiferimento);
    // Trova tutti i commenti di un utente per una specifica lettura
    List<CommentoPagina> findByLetturaCorrente(LetturaCorrente letturaCorrente);
    @Query("SELECT cp FROM CommentoPagina cp JOIN FETCH cp.utente WHERE cp.id = :id")
    Optional<CommentoPagina> findByIdWithUtente(Long id);
}

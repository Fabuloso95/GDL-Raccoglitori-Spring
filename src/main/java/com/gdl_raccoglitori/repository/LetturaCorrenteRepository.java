package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.gdl_raccoglitori.model.*;

public interface LetturaCorrenteRepository extends JpaRepository<LetturaCorrente, Long>
{
	Optional<LetturaCorrente> findById(Long id);
	List<LetturaCorrente> findByLibro(Libro libro);
	Optional<LetturaCorrente> findByPartecipaChiamataZoom(Boolean partecipaChiamataZoom);
	Optional<LetturaCorrente> findByUtenteAndLibro(Utente utente, Libro libro);
	List<LetturaCorrente> findByUtente(Utente utente);
	Set<LetturaCorrente> findByCommentiPagina(Set<CommentoPagina> commentiPagina);
	
	@Query("SELECT lc FROM LetturaCorrente lc " +
	           "LEFT JOIN FETCH lc.libro l " + 
	           "LEFT JOIN FETCH lc.commentiPagina cp " +
	           "WHERE lc.id = :id")
	    Optional<LetturaCorrente> findByIdWithEagerCollections(@Param("id") Long id);
    
	@Query("SELECT DISTINCT lc FROM LetturaCorrente lc " +
           "LEFT JOIN FETCH lc.libro l " + 
           "LEFT JOIN FETCH lc.commentiPagina cp " +
           "WHERE lc.utente = :utente")
    List<LetturaCorrente> findByUtenteWithEagerCollections(@Param("utente") Utente utente);
    
	@Query(value = "SELECT lc.id, u.username, lc.pagina_corrente, lc.partecipa_chiamata_zoom " +
            "FROM lettura_corrente lc JOIN utente u ON lc.utente_id = u.id " +
            "WHERE lc.libro_id = :libroId AND lc.data_completamento IS NULL",
    nativeQuery = true)
	List<Object[]> findProgressiUtentiByLibroId(Long libroId);
}

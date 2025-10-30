package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.gdl_raccoglitori.model.PropostaVoto;
import java.time.YearMonth;

public interface PropostaVotoRepository extends JpaRepository<PropostaVoto,	Long>
{
	Optional<PropostaVoto> findById(Long id);
	List<PropostaVoto> findByMeseVotazione(YearMonth meseVotazione); 
	// Trova la proposta con il maggior numero di voti per un dato mese (per decretare il vincitore)
	@Query("SELECT p FROM PropostaVoto p WHERE p.meseVotazione = :meseVotazione ORDER BY p.numVoti DESC LIMIT 1")
    PropostaVoto findTopPropostaByMeseVotazione(YearMonth meseVotazione);
}

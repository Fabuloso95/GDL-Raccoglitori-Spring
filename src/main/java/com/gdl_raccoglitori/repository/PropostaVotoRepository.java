package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.gdl_raccoglitori.model.PropostaVoto;

public interface PropostaVotoRepository extends JpaRepository<PropostaVoto,	Long>
{
	Optional<PropostaVoto> findById(Long id);
	List<PropostaVoto> findByMeseVotazione(String meseVotazione); 
	@Query("SELECT p FROM PropostaVoto p WHERE p.meseVotazione = :meseVotazione ORDER BY p.numVoti DESC LIMIT 1")
    PropostaVoto findTopPropostaByMeseVotazione(String meseVotazione);
}

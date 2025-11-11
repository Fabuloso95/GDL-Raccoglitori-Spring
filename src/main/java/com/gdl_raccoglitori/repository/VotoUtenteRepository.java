package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gdl_raccoglitori.model.*;

public interface VotoUtenteRepository extends JpaRepository<VotoUtente, Long>
{
	Optional<VotoUtente> findById(Long id);
	boolean existsByUtenteAndMeseVotazione(Utente utente, String meseVotazione);
	Optional<VotoUtente> findByUtenteAndPropostaVoto(Utente utente, PropostaVoto propostaVoto);
	List<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, String meseVotazione);
}

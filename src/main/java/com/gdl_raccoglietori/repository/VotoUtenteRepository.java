package com.gdl_raccoglietori.repository;

import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gdl_raccoglietori.model.*;
import com.gdl_raccoglietori.model.Utente;

public interface VotoUtenteRepository extends JpaRepository<VotoUtente, Long>
{
	Optional<VotoUtente> findById(Long id);
	boolean existsByUtenteAndMeseVotazione(Utente utente, YearMonth meseVotazione);
	Optional<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, YearMonth meseVotazione);
}

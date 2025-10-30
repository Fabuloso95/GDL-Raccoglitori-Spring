package com.gdl_raccoglitori.repository;

import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gdl_raccoglitori.model.*;

public interface VotoUtenteRepository extends JpaRepository<VotoUtente, Long>
{
	Optional<VotoUtente> findById(Long id);
	boolean existsByUtenteAndMeseVotazione(Utente utente, YearMonth meseVotazione);
	Optional<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, YearMonth meseVotazione);
}

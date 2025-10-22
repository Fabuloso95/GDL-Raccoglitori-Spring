package com.gdl_raccoglietori.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import com.gdl_raccoglietori.model.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long>
{
	public Optional<Utente> findById(Long id);
	public Optional<Utente> findByUsername(String username);
	public Optional<Utente> findByEmail(String email);
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Utente u WHERE u.username = :username")
    boolean existsByUsername(String username);
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Utente u WHERE u.email = :email")
	boolean existsByEmail(String email);
}

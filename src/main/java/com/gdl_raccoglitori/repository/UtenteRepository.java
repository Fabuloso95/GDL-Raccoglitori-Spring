package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.gdl_raccoglitori.model.*;

public interface UtenteRepository extends JpaRepository<Utente, Long>
{
	Optional<Utente> findById(Long id);
	Optional<Utente> findByUsername(String username);
	Optional<Utente> findByEmail(String email);
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Utente u WHERE u.username = :username")
    boolean existsByUsername(String username);
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Utente u WHERE u.email = :email")
	boolean existsByEmail(String email);
	public List<Utente> findByNomeContainingIgnoreCase(String nome);
	public List<Utente> findByCognomeContainingIgnoreCase(String cognome);
	public List<Utente> findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(String nome, String cognome);
	@Query("SELECT u FROM Utente u WHERE LOWER(u.username) LIKE CONCAT('%', LOWER(:query), '%') OR LOWER(u.nome) LIKE CONCAT('%', LOWER(:query), '%')")
    List<Utente> searchUtenti(@Param("query") String term);
	public Utente findByResetPasswordToken(String token);
	public List<Utente> findByRuolo(Ruolo ruolo);
	public Optional<Utente> findUtenteByEmail(String email);
	Optional<Utente> findByUsernameOrEmail(String username, String email);
	Optional<Utente> findByRefreshToken(String refreshToken); 
}

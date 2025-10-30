package com.gdl_raccoglitori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gdl_raccoglitori.model.*;
import java.util.*;

@Repository
public interface FrasePreferitaRepository extends JpaRepository<FrasePreferita, Long> 
{
	Optional<FrasePreferita> findById(Long id);
	
    // Trova tutte le frasi preferite salvate da un utente
    List<FrasePreferita> findByUtente(Utente utente);
    
    // Trova tutte le frasi preferite relative a un libro
    List<FrasePreferita> findByLibro(Libro libro);

    // Trova tutte le frasi preferite di un utente per un libro specifico
    List<FrasePreferita> findByUtenteAndLibro(Utente utente, Libro libro);
}

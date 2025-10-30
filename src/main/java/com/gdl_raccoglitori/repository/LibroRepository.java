package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.gdl_raccoglitori.model.Libro;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long>
{
	Optional<Libro> findById(Long id);
	boolean existsByTitolo(String titolo);
	Optional<Libro> findByTitolo(String titolo);
	List<Libro> findByAnnoPubblicazione(Integer annoPubblicazione);
	List<Libro> findByAutore(String autore);
	@Query("SELECT l FROM Libro l WHERE LOWER(l.titolo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(l.autore) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Libro> searchLibri(String searchTerm);
}

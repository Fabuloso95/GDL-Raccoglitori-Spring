package com.gdl_raccoglietori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gdl_raccoglietori.model.Libro;
import java.util.List;



public interface LibroRepository extends JpaRepository<Libro, Long>
{
	Optional<Libro> findById(Long id);
	boolean existsByTitolo(String titolo);
	Optional<Libro> findByTitolo(String titolo);
	List<Libro> findByAnnoPubblicazione(Integer annoPubblicazione);
	List<Libro> findByAutore(String autore);
}

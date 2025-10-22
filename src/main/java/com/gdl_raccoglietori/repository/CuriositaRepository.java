package com.gdl_raccoglietori.repository;

import com.gdl_raccoglietori.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CuriositaRepository extends JpaRepository<Curiosita, Long> 
{
	Optional<Curiosita> findById(Long id);
	
    // Trova tutte le curiosità relative a un libro specifico
    List<Curiosita> findByLibro(Libro libro);

    // Trova le curiosità relative a una pagina specifica di un libro
    List<Curiosita> findByLibroAndPaginaRiferimento(Libro libro, Integer paginaRiferimento);
}

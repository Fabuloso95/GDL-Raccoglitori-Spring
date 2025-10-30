package com.gdl_raccoglitori.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gdl_raccoglitori.model.*;
import java.util.*;

@Repository
public interface CuriositaRepository extends JpaRepository<Curiosita, Long> 
{
	Optional<Curiosita> findById(Long id);
    List<Curiosita> findByLibro(Libro libro);
    List<Curiosita> findByLibroAndPaginaRiferimento(Libro libro, Integer paginaRiferimento);
}

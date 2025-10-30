package com.gdl_raccoglitori.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gdl_raccoglitori.dto.request.LibroRequest;
import com.gdl_raccoglitori.dto.response.LibroResponse;
import com.gdl_raccoglitori.mapper.LibroMapper;
import com.gdl_raccoglitori.model.Libro;
import com.gdl_raccoglitori.repository.LibroRepository;
import com.gdl_raccoglitori.service.LibroService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class LibroServiceImpl implements LibroService 
{
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;

    private Libro findLibroById(Long id) 
    {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro non trovato con ID: " + id));
    }

    @Override
    @Transactional
    public LibroResponse creaLibro(LibroRequest request) 
    {
        log.info("Tentativo di creazione nuovo libro: {}", request.getTitolo());

        if (libroRepository.existsByTitolo(request.getTitolo())) 
        {
            throw new RuntimeException("Esiste già un libro con il titolo: " + request.getTitolo());
        }

        Libro nuovoLibro = libroMapper.toEntity(request);
        Libro savedLibro = libroRepository.save(nuovoLibro);
        
        log.info("Libro creato con successo, ID: {}", savedLibro.getId());
        return libroMapper.toResponse(savedLibro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> getAllLibri() 
    {
        log.info("Recupero tutti i libri.");
        return libroRepository.findAll().stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse getLibroById(Long id) 
    {
        log.info("Recupero libro con ID: {}", id);
        Libro libro = findLibroById(id);
        return libroMapper.toResponse(libro);
    }

    @Override
    @Transactional
    public LibroResponse aggiornaLibro(Long id, LibroRequest request) 
    {
        log.info("Aggiornamento libro con ID: {}", id);
        
        Libro libroEsistente = findLibroById(id);

        if (!libroEsistente.getTitolo().equals(request.getTitolo()) && libroRepository.existsByTitolo(request.getTitolo()))
        {
             throw new RuntimeException("Impossibile aggiornare: esiste già un altro libro con il titolo: " + request.getTitolo());
        }

        libroMapper.updateLibroFromRequest(request, libroEsistente);

        Libro updatedLibro = libroRepository.save(libroEsistente);
        log.info("Libro ID {} aggiornato con successo.", id);
        return libroMapper.toResponse(updatedLibro);
    }

    @Override
    @Transactional
    public void eliminaLibro(Long id) 
    {
        log.warn("Tentativo di eliminazione libro con ID: {}", id);
        
        Libro libro = findLibroById(id); 
        libroRepository.delete(libro);
        
        log.info("Libro ID {} eliminato con successo.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> cercaLibri(String searchTerm) 
    {
        log.info("Ricerca libri per termine: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) 
        {
            return getAllLibri();
        }
        
        List<Libro> risultati = libroRepository.searchLibri(searchTerm);
        
        log.info("Trovati {} risultati per la ricerca di '{}'.", risultati.size(), searchTerm);
        
        return risultati.stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }
}

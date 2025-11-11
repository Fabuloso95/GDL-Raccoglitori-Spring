package com.gdl_raccoglitori.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.exceptionhandler.exception.*;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.*;
import com.gdl_raccoglitori.service.CuriositaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuriositaServiceImpl implements CuriositaService 
{
    private final CuriositaRepository curiositaRepository;
    private final LibroRepository libroRepository;

    @Override
    public Curiosita createCuriosita(CuriositaRequest request, Utente utenteCreatore) 
    {
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + request.getLibroId() + " non trovato."));

        Curiosita curiosita = new Curiosita();
        curiosita.setUtenteCreatore(utenteCreatore);
        curiosita.setLibro(libro);
        curiosita.setTitolo(request.getTitolo());
        curiosita.setContenuto(request.getContenuto());
        curiosita.setPaginaRiferimento(request.getPaginaRiferimento());
        
        log.info("Creazione nuova curiosità per il libro {} da parte dell'utente {}", 
                 libro.getTitolo(), utenteCreatore.getUsername());

        return curiositaRepository.save(curiosita);
    }

    @Override
    public Curiosita updateCuriosita(Long curiositaId, CuriositaRequest request, Utente utenteRichiedente) 
    {
        Curiosita curiosita = findById(curiositaId);

        if (!curiosita.getUtenteCreatore().getId().equals(utenteRichiedente.getId())) 
        {
            log.warn("Tentativo non autorizzato di aggiornare la curiosità ID {} da parte dell'utente ID {}",
                     curiositaId, utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad aggiornare questa curiosità. Solo il creatore può farlo.");
        }

        curiosita.setTitolo(request.getTitolo());
        curiosita.setContenuto(request.getContenuto());
        curiosita.setPaginaRiferimento(request.getPaginaRiferimento());
        
        return curiositaRepository.save(curiosita);
    }

    @Override
    public void deleteCuriosita(Long curiositaId, Utente utenteRichiedente) 
    {
        Curiosita curiosita = findById(curiositaId);

        boolean isCreator = curiosita.getUtenteCreatore().getId().equals(utenteRichiedente.getId());
        boolean isAdminOrModerator = utenteRichiedente.getRuolo().name().equals("ADMIN");

        if (!isCreator && !isAdminOrModerator) 
        {
            log.warn("Tentativo non autorizzato di eliminare la curiosità ID {} da parte dell'utente ID {}",
                     curiositaId, utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad eliminare questa curiosità.");
        }

        log.info("Eliminazione curiosità ID {} da parte dell'utente ID {}", curiositaId, utenteRichiedente.getId());
        curiositaRepository.delete(curiosita);
    }

    @Override
    public Curiosita findById(Long id) 
    {
        return curiositaRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Curiosità con ID " + id + " non trovata."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curiosita> findByLibroId(Long libroId) 
    {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + libroId + " non trovato."));
        
        return curiositaRepository.findByLibro(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curiosita> findByLibroAndPagina(Long libroId, Integer paginaRiferimento) 
    {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + libroId + " non trovato."));
        
        return curiositaRepository.findByLibroAndPaginaRiferimento(libro, paginaRiferimento);
    }
}

package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.exceptionhandler.exception.*;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.*;
import com.gdl_raccoglitori.service.LetturaCorrenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetturaCorrenteServiceImpl implements LetturaCorrenteService 
{
    private final LetturaCorrenteRepository letturaCorrenteRepository;
    private final LibroRepository libroRepository;

    private void checkAuthorization(LetturaCorrente lettura, Utente utenteRichiedente, String operation) 
    {
        if (!lettura.getUtente().getId().equals(utenteRichiedente.getId())) 
        {
            log.warn("Tentativo non autorizzato di {} la lettura ID {} da parte dell'utente ID {}",
                     operation, lettura.getId(), utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad eseguire questa operazione. La lettura appartiene ad un altro utente.");
        }
    }

    @Override
    @Transactional
    public LetturaCorrente startNewReading(LetturaCorrenteRequest request, Utente utente) 
    {
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + request.getLibroId() + " non trovato."));

        if (letturaCorrenteRepository.findByUtenteAndLibro(utente, libro).isPresent()) 
        {
            throw new ConflittoDatiException("L'utente ha già una sessione di lettura in corso per il libro " + libro.getTitolo());
        }

        LetturaCorrente lettura = new LetturaCorrente();
        lettura.setUtente(utente);
        lettura.setLibro(libro);
        lettura.setPaginaCorrente(request.getPaginaIniziale() != null ? request.getPaginaIniziale() : 0);
        lettura.setDataInizio(LocalDate.now());
        lettura.setPartecipaChiamataZoom(false);
        
        log.info("Iniziata nuova lettura per il libro {} da parte dell'utente {}", 
                 libro.getTitolo(), utente.getUsername());

        return letturaCorrenteRepository.save(lettura);
    }

    @Override
    @Transactional
    public LetturaCorrente updateReadingProgress(Long letturaId, LetturaCorrenteUpdateRequest request, Utente utenteRichiedente) 
    {
        LetturaCorrente lettura = findById(letturaId); 
        checkAuthorization(lettura, utenteRichiedente, "aggiornare");

        if (lettura.getDataCompletamento() != null)
        {
             throw new ConflittoDatiException("Impossibile aggiornare una lettura già completata.");
        }
        
        lettura.setPaginaCorrente(request.getPaginaCorrente());
        if (request.getPartecipaChiamataZoom() != null)
        {
            lettura.setPartecipaChiamataZoom(request.getPartecipaChiamataZoom());
        }
        
        log.debug("Lettura ID {} aggiornata: Pagina {} / Partecipa Zoom {}", 
                  letturaId, lettura.getPaginaCorrente(), lettura.getPartecipaChiamataZoom());

        return letturaCorrenteRepository.save(lettura);
    }

    @Override
    @Transactional
    public LetturaCorrente completeReading(Long letturaId, Utente utenteRichiedente) 
    {
        LetturaCorrente lettura = findById(letturaId);
        checkAuthorization(lettura, utenteRichiedente, "completare");

        if (lettura.getDataCompletamento() == null)
        {
            lettura.setDataCompletamento(LocalDate.now());
            lettura.setPartecipaChiamataZoom(false); 
            log.info("Lettura ID {} completata dall'utente {}", letturaId, utenteRichiedente.getUsername());
            return letturaCorrenteRepository.save(lettura);
        }
        
        return lettura;
    }

    @Override
    @Transactional
    public void deleteReading(Long letturaId, Utente utenteRichiedente) 
    {
        LetturaCorrente lettura = findById(letturaId);
        checkAuthorization(lettura, utenteRichiedente, "eliminare");

        log.warn("Eliminazione lettura ID {} da parte dell'utente {}", letturaId, utenteRichiedente.getUsername());
        letturaCorrenteRepository.delete(lettura);
    }

    @Override
    @Transactional(readOnly = true) 
    public LetturaCorrente findById(Long id) 
    {
        return letturaCorrenteRepository.findByIdWithEagerCollections(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Lettura Corrente con ID " + id + " non trovata."));
    }

    @Override
    @Transactional(readOnly = true) 
    public List<LetturaCorrente> findByUtente(Utente utente) 
    {
        log.debug("Recupero letture per utente ID {} con caricamento eager delle collezioni.", utente.getId());
        return letturaCorrenteRepository.findByUtenteWithEagerCollections(utente);
    }

    @Override   
    @Transactional(readOnly = true) 
    public List<Object[]> findUsersProgressByLibroId(Long libroId) 
    {
        if (!libroRepository.existsById(libroId))
        {
            throw new RisorsaNonTrovataException("Libro con ID " + libroId + " non trovato.");
        }
        
        return letturaCorrenteRepository.findProgressiUtentiByLibroId(libroId);
    }
}

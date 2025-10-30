package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglitori.exceptionhandler.exception.OperazioneNonAutorizzataException;
import com.gdl_raccoglitori.exceptionhandler.exception.RisorsaNonTrovataException;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.*;
import com.gdl_raccoglitori.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentoPaginaServiceImpl implements CommentoPaginaService 
{
    private final CommentoPaginaRepository commentoPaginaRepository;
    private final UtenteRepository utenteRepository;
    private final LetturaCorrenteRepository letturaCorrenteRepository;

    @Override
    public CommentoPagina createCommento(CommentoPaginaRequest request, Utente utenteAutore) 
    {
        LetturaCorrente letturaCorrente = letturaCorrenteRepository.findById(request.getLetturaCorrenteId())
                .orElseThrow(() -> new RisorsaNonTrovataException("LetturaCorrente con ID " + request.getLetturaCorrenteId() + " non trovata."));

        CommentoPagina commento = new CommentoPagina();
        commento.setUtente(utenteAutore);
        commento.setLetturaCorrente(letturaCorrente);
        commento.setPaginaRiferimento(request.getPaginaRiferimento());
        commento.setContenuto(request.getContenuto());
        commento.setDataCreazione(LocalDateTime.now());
        
        log.info("Creazione nuovo commento per la lettura {} sulla pagina {}", 
                 letturaCorrente.getId(), commento.getPaginaRiferimento());

        return commentoPaginaRepository.save(commento);
    }

    @Override
    public CommentoPagina updateCommentoContenuto(Long commentoId, String contenuto, Utente utenteRichiedente) 
    {
        CommentoPagina commento = findById(commentoId);

        if (!commento.getUtente().getId().equals(utenteRichiedente.getId())) 
        {
            log.warn("Tentativo non autorizzato di aggiornare il commento ID {} da parte dell'utente ID {}",
                     commentoId, utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad aggiornare questo commento. Solo l'autore può farlo.");
        }

        commento.setContenuto(contenuto);
        
        return commentoPaginaRepository.save(commento);
    }

    @Override
    public void deleteCommento(Long commentoId, Utente utenteRichiedente) 
    {
        CommentoPagina commento = findById(commentoId);

        boolean isAuthor = commento.getUtente().getId().equals(utenteRichiedente.getId());
        boolean isAdminOrModerator = utenteRichiedente.getRuolo() == Ruolo.ADMIN;

        if (!isAuthor && !isAdminOrModerator) 
        {
            log.warn("Tentativo non autorizzato di eliminare il commento ID {} da parte dell'utente ID {}",
                     commentoId, utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad eliminare questo commento.");
        }

        log.info("Eliminazione commento ID {} da parte dell'utente ID {}", commentoId, utenteRichiedente.getId());
        commentoPaginaRepository.delete(commento);
    }

    @Override
    public CommentoPagina findById(Long id) 
    {
        return commentoPaginaRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("CommentoPagina con ID " + id + " non trovato."));
    }

    @Override
    public List<CommentoPagina> findByLetturaAndPagina(Long letturaCorrenteId, Integer paginaRiferimento) 
    {
        LetturaCorrente letturaCorrente = letturaCorrenteRepository.findById(letturaCorrenteId)
                .orElseThrow(() -> new RisorsaNonTrovataException("LetturaCorrente con ID " + letturaCorrenteId + " non trovata."));
        
        return commentoPaginaRepository.findByLetturaCorrenteAndPaginaRiferimentoOrderByDataCreazioneAsc(
                letturaCorrente, paginaRiferimento);
    }
    
    @Override
    public List<CommentoPagina> findByAutoreId(Long utenteId) 
    {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente con ID " + utenteId + " non trovato."));
        
        return commentoPaginaRepository.findByUtente(utente);
    }
}

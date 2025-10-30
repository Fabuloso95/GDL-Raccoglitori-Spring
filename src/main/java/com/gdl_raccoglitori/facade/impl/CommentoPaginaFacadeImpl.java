package com.gdl_raccoglitori.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.gdl_raccoglitori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglitori.dto.response.CommentoPaginaResponse;
import com.gdl_raccoglitori.exceptionhandler.exception.OperazioneNonAutorizzataException;
import com.gdl_raccoglitori.facade.CommentoPaginaFacade;
import com.gdl_raccoglitori.mapper.CommentoPaginaMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.CommentoPaginaService;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentoPaginaFacadeImpl implements CommentoPaginaFacade 
{
    private final CommentoPaginaService commentoPaginaService;
    private final CommentoPaginaMapper commentoPaginaMapper;

    private Utente getCurrentAuthenticatedUser() 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof Utente)) 
        {
            log.error("Tentativo di accesso non autorizzato a un'operazione sui commenti. Nessun utente Utente trovato nel contesto.");
            throw new OperazioneNonAutorizzataException("Utente non autenticato o il Principal non è un oggetto Utente valido.");
        }
        
        return (Utente) authentication.getPrincipal(); 
    }


    @Override
    public CommentoPaginaResponse createCommento(CommentoPaginaRequest request) 
    {
        Utente autore = getCurrentAuthenticatedUser();
        log.debug("Utente autenticato ID {} sta creando un commento.", autore.getId());
        CommentoPagina commento = commentoPaginaService.createCommento(request, autore);
        
        return commentoPaginaMapper.toResponse(commento);
    }

    @Override
    public CommentoPaginaResponse updateCommento(Long commentoId, String nuovoContenuto) 
    {
        Utente richiedente = getCurrentAuthenticatedUser();
        log.debug("Utente autenticato ID {} sta aggiornando il commento ID {}.", richiedente.getId(), commentoId);
        CommentoPagina commento = commentoPaginaService.updateCommentoContenuto(commentoId, nuovoContenuto, richiedente);
        
        return commentoPaginaMapper.toResponse(commento);
    }

    @Override
    public void deleteCommento(Long commentoId) 
    {
        Utente richiedente = getCurrentAuthenticatedUser();
        log.warn("Utente autenticato ID {} sta tentando di eliminare il commento ID {}.", richiedente.getId(), commentoId);
        commentoPaginaService.deleteCommento(commentoId, richiedente);
    }

    @Override
    public CommentoPaginaResponse findById(Long id) 
    {
        CommentoPagina commento = commentoPaginaService.findById(id);
        return commentoPaginaMapper.toResponse(commento);
    }

    @Override
    public List<CommentoPaginaResponse> findByLetturaAndPagina(Long letturaCorrenteId, Integer paginaRiferimento) 
    {
        List<CommentoPagina> commenti = commentoPaginaService.findByLetturaAndPagina(letturaCorrenteId, paginaRiferimento);
        
        return commenti.stream()
                .map(commentoPaginaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentoPaginaResponse> findByAutoreId(Long utenteId) 
    {
        List<CommentoPagina> commenti = commentoPaginaService.findByAutoreId(utenteId);
        
        return commenti.stream()
                .map(commentoPaginaMapper::toResponse)
                .collect(Collectors.toList());
    }
}

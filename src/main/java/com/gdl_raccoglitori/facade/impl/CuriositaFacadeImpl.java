package com.gdl_raccoglitori.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.dto.response.CuriositaResponse;
import com.gdl_raccoglitori.exceptionhandler.exception.OperazioneNonAutorizzataException;
import com.gdl_raccoglitori.facade.CuriositaFacade;
import com.gdl_raccoglitori.mapper.CuriositaMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CuriositaFacadeImpl implements CuriositaFacade 
{
    private final CuriositaService curiositaService;
    private final CuriositaMapper curiositaMapper;
    private final UtenteService utenteService; 

    private Utente getCurrentAuthenticatedUser() 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) 
        {
            log.error("Tentativo di accesso non autorizzato: Contesto di sicurezza vuoto o non autenticato.");
            throw new OperazioneNonAutorizzataException("Utente non autenticato o il Principal non è valido.");
        }
        
        Object principal = authentication.getPrincipal();
        String tempUsername = null; 
        
        if (principal instanceof Utente) 
        {
            log.debug("Utente recuperato direttamente come Utente completo.");
            return (Utente) principal;
            
        } 
        else if 
        (principal instanceof UserDetails)
{
            tempUsername = ((UserDetails) principal).getUsername();
            
        } 
        else if (principal instanceof String) 
        {
            tempUsername = (String) principal;
        }

        final String username = tempUsername;

        if (username != null)
        {
            try 
            {
                log.debug("Caricamento Utente dal servizio per username: {}", username);
                return utenteService.findByUsername(username)
                    .orElseThrow(() -> new OperazioneNonAutorizzataException("Utente autenticato non trovato nel database: " + username));
            } 
            catch (OperazioneNonAutorizzataException e) 
            {
                 throw e;
            }
            catch (Exception e)
            {
                log.error("Impossibile trovare Utente con username {}: {}", username, e.getMessage());
                throw new OperazioneNonAutorizzataException("Errore nel recupero dell'utente dal database.");
            }
        }
        
        log.error("Tentativo di accesso non autorizzato: Tipo di Principal sconosciuto: {}", principal.getClass().getName());
        throw new OperazioneNonAutorizzataException("Tipo di Principal non gestito nel contesto di sicurezza.");
    }

    @Override
    public CuriositaResponse createCuriosita(CuriositaRequest request) 
    {
        Utente utenteCreatore = getCurrentAuthenticatedUser();
        log.debug("Utente autenticato ID {} sta creando una curiosità per il libro ID {}", utenteCreatore.getId(), request.getLibroId());
        
        Curiosita curiosita = curiositaService.createCuriosita(request, utenteCreatore);
        
        return curiositaMapper.toResponse(curiosita);
    }

    @Override
    public CuriositaResponse updateCuriosita(Long curiositaId, CuriositaRequest request) 
    {
        Utente richiedente = getCurrentAuthenticatedUser();
        log.debug("Utente autenticato ID {} sta aggiornando la curiosità ID {}.", richiedente.getId(), curiositaId);
        
        Curiosita updatedCuriosita = curiositaService.updateCuriosita(curiositaId, request, richiedente);
        
        return curiositaMapper.toResponse(updatedCuriosita);
    }

    @Override
    public void deleteCuriosita(Long curiositaId) 
    {
        Utente richiedente = getCurrentAuthenticatedUser();
        log.warn("Utente autenticato ID {} sta tentando di eliminare la curiosità ID {}.", richiedente.getId(), curiositaId);
        
        curiositaService.deleteCuriosita(curiositaId, richiedente);
    }

    @Override
    public CuriositaResponse findById(Long id) 
    {
        Curiosita curiosita = curiositaService.findById(id);
        return curiositaMapper.toResponse(curiosita);
    }

    @Override
    public List<CuriositaResponse> findByLibroId(Long libroId) 
    {
        List<Curiosita> curiositaList = curiositaService.findByLibroId(libroId);
        
        return curiositaList.stream()
                .map(curiositaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CuriositaResponse> findByLibroAndPagina(Long libroId, Integer paginaRiferimento) 
    {
        List<Curiosita> curiositaList = curiositaService.findByLibroAndPagina(libroId, paginaRiferimento);
        
        return curiositaList.stream()
                .map(curiositaMapper::toResponse)
                .collect(Collectors.toList());
    }
}

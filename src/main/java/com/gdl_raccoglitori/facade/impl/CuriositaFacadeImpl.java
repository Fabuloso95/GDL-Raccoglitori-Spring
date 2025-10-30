package com.gdl_raccoglitori.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.dto.response.CuriositaResponse;
import com.gdl_raccoglitori.exceptionhandler.exception.OperazioneNonAutorizzataException;
import com.gdl_raccoglitori.facade.CuriositaFacade;
import com.gdl_raccoglitori.mapper.CuriositaMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.CuriositaService;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CuriositaFacadeImpl implements CuriositaFacade 
{
    private final CuriositaService curiositaService;
    private final CuriositaMapper curiositaMapper;

    private Utente getCurrentAuthenticatedUser() 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof Utente)) 
        {
            log.error("Tentativo di accesso non autorizzato a un'operazione sulle curiosità. Nessun utente Utente trovato nel contesto.");
            throw new OperazioneNonAutorizzataException("Utente non autenticato o il Principal non è un oggetto Utente valido.");
        }
        
        return (Utente) authentication.getPrincipal(); 
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
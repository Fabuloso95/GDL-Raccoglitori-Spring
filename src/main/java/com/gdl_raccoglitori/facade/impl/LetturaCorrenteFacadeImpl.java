package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.exceptionhandler.exception.OperazioneNonAutorizzataException;
import com.gdl_raccoglitori.facade.LetturaCorrenteFacade;
import com.gdl_raccoglitori.mapper.LetturaCorrenteMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.LetturaCorrenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LetturaCorrenteFacadeImpl implements LetturaCorrenteFacade 
{
    private final LetturaCorrenteService letturaCorrenteService;
    private final LetturaCorrenteMapper letturaCorrenteMapper;

    private Utente getCurrentAuthenticatedUser() 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof Utente)) 
        {
            log.error("Tentativo di accesso non autorizzato. Nessun utente Utente trovato nel contesto.");
            throw new OperazioneNonAutorizzataException("Utente non autenticato.");
        }
        
        return (Utente) authentication.getPrincipal(); 
    }

    @Override
    public LetturaCorrenteResponse startNewReading(LetturaCorrenteRequest request) 
    {
        Utente utente = getCurrentAuthenticatedUser();
        log.debug("Utente ID {} sta iniziando una nuova lettura per il libro ID {}", utente.getId(), request.getLibroId());
        
        LetturaCorrente lettura = letturaCorrenteService.startNewReading(request, utente);
        
        return letturaCorrenteMapper.toResponse(lettura);
    }

    @Override
    public LetturaCorrenteResponse updateReadingProgress(Long letturaId, LetturaCorrenteUpdateRequest request) 
    {
        Utente utente = getCurrentAuthenticatedUser();
        log.debug("Utente ID {} sta aggiornando la lettura ID {}", utente.getId(), letturaId);
        
        LetturaCorrente lettura = letturaCorrenteService.updateReadingProgress(letturaId, request, utente);
        
        return letturaCorrenteMapper.toResponse(lettura);
    }

    @Override
    public LetturaCorrenteResponse completeReading(Long letturaId) 
    {
        Utente utente = getCurrentAuthenticatedUser();
        log.info("Utente ID {} sta completando la lettura ID {}", utente.getId(), letturaId);
        
        LetturaCorrente lettura = letturaCorrenteService.completeReading(letturaId, utente);
        
        return letturaCorrenteMapper.toResponse(lettura);
    }

    @Override
    public void deleteReading(Long letturaId) 
    {
        Utente utente = getCurrentAuthenticatedUser();
        log.warn("Utente ID {} sta eliminando la lettura ID {}", utente.getId(), letturaId);
        
        letturaCorrenteService.deleteReading(letturaId, utente);
    }

    @Override
    public LetturaCorrenteResponse findById(Long id) 
    {
        LetturaCorrente lettura = letturaCorrenteService.findById(id);
        return letturaCorrenteMapper.toResponse(lettura);
    }

    @Override
    public List<LetturaCorrenteResponse> findMyReadings() 
    {
        Utente utente = getCurrentAuthenticatedUser();
        log.debug("Recupero letture correnti per l'utente ID {}", utente.getId());
        
        List<LetturaCorrente> lettureList = letturaCorrenteService.findByUtente(utente);
        
        return lettureList.stream()
                .map(letturaCorrenteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LetturaCorrenteProgressResponse> getBookProgressOverview(Long libroId) 
    {
        log.debug("Recupero progressi utenti per il libro ID {}", libroId);
        
        List<Object[]> progressiRaw = letturaCorrenteService.findUsersProgressByLibroId(libroId);

        return progressiRaw.stream()
                .map(row -> LetturaCorrenteProgressResponse.builder()
                    .letturaCorrenteId((Long) row[0])
                    .username((String) row[1])
                    .paginaCorrente((Integer) row[2])
                    .partecipaChiamataZoom((Boolean) row[3])
                    .build())
                .collect(Collectors.toList());
    }
}

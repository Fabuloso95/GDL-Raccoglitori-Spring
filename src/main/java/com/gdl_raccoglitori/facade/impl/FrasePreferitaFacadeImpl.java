package com.gdl_raccoglitori.facade.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.gdl_raccoglitori.dto.request.FrasePreferitaRequest;
import com.gdl_raccoglitori.dto.response.FrasePreferitaResponse;
import com.gdl_raccoglitori.exceptionhandler.exception.OperazioneNonAutorizzataException;
import com.gdl_raccoglitori.facade.FrasePreferitaFacade;
import com.gdl_raccoglitori.mapper.FrasePreferitaMapper;
import com.gdl_raccoglitori.model.FrasePreferita;
import com.gdl_raccoglitori.model.Utente;
import com.gdl_raccoglitori.service.FrasePreferitaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FrasePreferitaFacadeImpl implements FrasePreferitaFacade
{
	private final FrasePreferitaService frasePreferitaService;
	private final FrasePreferitaMapper frasePreferitaMapper;
	
	private Utente getCurrentAuthenticatedUser() 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof Utente)) 
        {
            log.error("Tentativo di accesso non autorizzato a un'operazione sulle frasi preferite. Nessun utente Utente trovato nel contesto.");
            throw new OperazioneNonAutorizzataException("Utente non autenticato o il Principal non è un oggetto Utente valido.");
        }
        
        return (Utente) authentication.getPrincipal(); 
    }
	
	@Override
	public FrasePreferitaResponse saveFrase(FrasePreferitaRequest request) 
	{
		Utente utenteSalvatore = getCurrentAuthenticatedUser();
        log.debug("Utente autenticato ID {} sta salvando una frase per il libro ID {}", utenteSalvatore.getId(), request.getLibroId());

        FrasePreferita frase = frasePreferitaService.saveFrasePreferita(request, utenteSalvatore);
		
        return frasePreferitaMapper.toResponse(frase);
	}

	@Override
	public void deleteFrase(Long fraseId)
	{
		Utente richiedente = getCurrentAuthenticatedUser();
        log.warn("Utente autenticato ID {} sta tentando di eliminare la frase preferita ID {}.", richiedente.getId(), fraseId);
        
        frasePreferitaService.deleteFrasePreferita(fraseId, richiedente);
	}

	@Override
	public FrasePreferitaResponse findById(Long id)
	{
		FrasePreferita frase = frasePreferitaService.findById(id);
		
		return frasePreferitaMapper.toResponse(frase);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FrasePreferitaResponse> findMyFrasi()
	{
		Utente utenteSalvatore = getCurrentAuthenticatedUser();
        log.debug("Recupero frasi preferite per l'utente ID {}", utenteSalvatore.getId());
        List<FrasePreferita> frasi = frasePreferitaService.findByUtente(utenteSalvatore);
		
        return frasi.stream()
				.map(frasePreferitaMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<FrasePreferitaResponse> findByLibroId(Long libroId) 
	{
		log.debug("Recupero frasi preferite per il libro ID {}", libroId);
		List<FrasePreferita> frasi = frasePreferitaService.findByLibroId(libroId);
		
		return frasi.stream()
				.map(frasePreferitaMapper::toResponse)
				.collect(Collectors.toList());
	}
}

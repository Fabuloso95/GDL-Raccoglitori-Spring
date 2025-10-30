package com.gdl_raccoglitori.facade.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.gdl_raccoglitori.dto.request.LibroRequest;
import com.gdl_raccoglitori.dto.response.LibroResponse;
import com.gdl_raccoglitori.facade.LibroFacade;
import com.gdl_raccoglitori.service.LibroService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class LibroFacadeImpl implements LibroFacade 
{
	private final LibroService libroService;
	
	@Override
	public LibroResponse creaLibro(LibroRequest request) 
	{
		log.debug("Facade: Richiesta creazione libro per il titolo: {}", request.getTitolo());
		return libroService.creaLibro(request);
	}

	@Override
	public List<LibroResponse> getAllLibri() 
	{
		log.debug("Facade: Richiesta recupero di tutti i libri.");
		return libroService.getAllLibri();
	}

	@Override
	public LibroResponse getLibroById(Long id)
	{
		log.debug("Facade: Richiesta recupero libro con ID: {}", id);
		return libroService.getLibroById(id);
	}

	@Override
	public LibroResponse aggiornaLibro(Long id, LibroRequest request) 
	{
		log.debug("Facade: Richiesta aggiornamento libro con ID: {}", id);
		return libroService.aggiornaLibro(id, request);
	}

	@Override
	public void eliminaLibro(Long id)
	{
		log.debug("Facade: Richiesta eliminazione libro con ID: {}", id);
		libroService.eliminaLibro(id);
	}

	@Override
	public List<LibroResponse> cercaLibri(String searchTerm) 
	{
		log.debug("Facade: Richiesta ricerca libri con termine: {}", searchTerm);
		return libroService.cercaLibri(searchTerm);
	}
}

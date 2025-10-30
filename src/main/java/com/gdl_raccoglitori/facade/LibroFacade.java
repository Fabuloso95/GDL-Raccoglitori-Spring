package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.request.LibroRequest;
import com.gdl_raccoglitori.dto.response.LibroResponse;

public interface LibroFacade 
{
	LibroResponse creaLibro(LibroRequest request);
	List<LibroResponse> getAllLibri();
	LibroResponse getLibroById(Long id);
	LibroResponse aggiornaLibro(Long id, LibroRequest request);
	void eliminaLibro(Long id);
	List<LibroResponse> cercaLibri(String searchTerm);
}

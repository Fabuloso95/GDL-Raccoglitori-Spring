package com.gdl_raccoglietori.service;

import java.util.List;
import com.gdl_raccoglietori.dto.request.LibroRequest;
import com.gdl_raccoglietori.dto.response.LibroResponse;

public interface LibroService 
{
	LibroResponse creaLibro(LibroRequest request);
	List<LibroResponse> getAllLibri();
	LibroResponse getLibroById(Long id);
	LibroResponse aggiornaLibro(Long id, LibroRequest request);
	void eliminaLibro(Long id);
	List<LibroResponse> cercaLibri(String searchTerm);
}

package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglitori.dto.request.FrasePreferitaRequest;
import com.gdl_raccoglitori.dto.response.FrasePreferitaResponse;
import com.gdl_raccoglitori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FrasePreferitaMapper 
{
	@Mapping(source = "libro.id", target = "libroId")
	@Mapping(source = "utente.id", target = "utenteId")
	FrasePreferitaResponse toResponse(FrasePreferita frasePreferita);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "libroId", target = "libro")
	@Mapping(source = "utenteId", target = "utente")
	FrasePreferita toEntity(FrasePreferitaRequest request);
	
	default Libro toLibro(Long id)
	{
		if(id == null) return null;
		Libro libro = new Libro();
		libro.setId(id);
		return libro;
	}
	
	default Utente toUtente(Long id)
	{
		if(id == null) return null;
		Utente utente = new Utente();
		utente.setId(id);
		return utente;
	}
}

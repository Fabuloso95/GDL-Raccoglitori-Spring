package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglietori.dto.response.FrasePreferitaResponse;
import com.gdl_raccoglietori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FrasePreferitaMapper 
{
	@Mapping(source = "libro.id", target = "libroId")
	@Mapping(source = "utente.id", target = "utenteId")
	FrasePreferitaResponse toResponse(FrasePreferita frasePreferita);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "libroId", target = "libro")
	@Mapping(source = "utenteId", target = "utente")
	FrasePreferita toEntity(FrasePreferitaResponse frasePreferitaResponse);
	
	default Libro toLibro(Long id)
	{
		if(id == null) return null;
		Libro libro = new Libro();
		libro.setId(id);
		return libro;
	}
}

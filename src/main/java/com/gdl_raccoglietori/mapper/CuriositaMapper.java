package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;

import com.gdl_raccoglietori.dto.request.CuriositaRequest;
import com.gdl_raccoglietori.dto.response.CuriositaResponse;
import com.gdl_raccoglietori.model.Curiosita;
import com.gdl_raccoglietori.model.Libro;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AutoreMapper.class})
public interface CuriositaMapper 
{
	@Mapping(source = "libro.id", target = "libroId")
	CuriositaResponse toResponse(Curiosita curiosita);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "utenteCreatore", ignore = true)
	@Mapping(source = "libroId", target = "libro")
	Curiosita toEntity(CuriositaRequest curiositaRequest);
	
	default Libro toLibro(Long id)
	{
        if (id == null) return null;
        Libro libro = new Libro();
        libro.setId(id);
        return libro;
    }
}

package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import org.mapstruct.Mapper;
import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.dto.response.CuriositaResponse;
import com.gdl_raccoglitori.model.Curiosita;
import com.gdl_raccoglitori.model.Libro;

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

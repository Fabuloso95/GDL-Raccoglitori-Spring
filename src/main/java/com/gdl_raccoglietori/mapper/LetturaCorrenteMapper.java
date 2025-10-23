package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglietori.dto.request.LetturaCorrenteRequest;
import com.gdl_raccoglietori.dto.response.LetturaCorrenteResponse;
import com.gdl_raccoglietori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentoPaginaMapper.class})
public interface LetturaCorrenteMapper
{
	@Mapping(source = "libro.id", target = "libroId")
	@Mapping(source = "utente.id", target = "utenteId")
	LetturaCorrenteResponse toResponse(LetturaCorrente letturaCorrente);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "utente", ignore = true)
	@Mapping(target = "dataInizio", ignore = true)
	@Mapping(target = "dataCompletamento", ignore = true)
	@Mapping(target = "commentiPagina", ignore = true)
	@Mapping(target = "partecipaChiamataZoom", ignore = true)
	@Mapping(source = "paginaIniziale", target = "paginaCorrente")
	@Mapping(source = "libroId", target = "libro")
	LetturaCorrente toEntity(LetturaCorrenteRequest request);
	
	default Libro toLibro(Long id)
	{
		if (id == null) return null;
		Libro libro = new Libro();
		libro.setId(id);
		return libro;
	}
	
	default Utente toUtente(Long id)
	{
		if (id == null) return null;
		Utente utente = new Utente();
		utente.setId(id);
		return utente;
	}
}

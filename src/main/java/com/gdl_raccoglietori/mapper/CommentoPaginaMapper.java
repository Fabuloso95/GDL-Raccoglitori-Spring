package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglietori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglietori.dto.response.CommentoPaginaResponse;
import com.gdl_raccoglietori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UtenteMapper.class})
public interface CommentoPaginaMapper 
{
	@Mapping(source = "letturaCorrente.id", target = "letturaCorrenteId")
    CommentoPaginaResponse toResponse(CommentoPagina commentoPagina);
	
	@Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCreazione", ignore = true)
    @Mapping(target = "utente", ignore = true) 
    @Mapping(source = "letturaCorrenteId", target = "letturaCorrente")
    CommentoPagina toEntity(CommentoPaginaRequest request);
	
	default LetturaCorrente toLetturaCorrente(Long id) 
	{
        if (id == null) return null;
        LetturaCorrente lc = new LetturaCorrente();
        lc.setId(id);
        return lc;
    }
}

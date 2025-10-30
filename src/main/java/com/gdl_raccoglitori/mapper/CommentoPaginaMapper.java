package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglitori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglitori.dto.response.CommentoPaginaResponse;
import com.gdl_raccoglitori.model.*;

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

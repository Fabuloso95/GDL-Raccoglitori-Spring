package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglitori.dto.response.AutoreCommentoResponse;
import com.gdl_raccoglitori.model.Utente;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AutoreMapper 
{
	AutoreCommentoResponse toAutoreResponse(Utente utente);
}

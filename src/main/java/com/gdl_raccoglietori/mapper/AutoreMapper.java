package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglietori.dto.response.AutoreCommentoResponse;
import com.gdl_raccoglietori.model.Utente;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AutoreMapper 
{
	AutoreCommentoResponse toAutoreResponse(Utente utente);
}

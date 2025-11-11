package com.gdl_raccoglitori.mapper;

import com.gdl_raccoglitori.dto.response.ImpostazioniResponse;
import com.gdl_raccoglitori.model.Impostazioni;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImpostazioniMapper 
{
    
    ImpostazioniMapper INSTANCE = Mappers.getMapper(ImpostazioniMapper.class);
    
    @Mapping(source = "utente.id", target = "utenteId")
    ImpostazioniResponse toResponse(Impostazioni impostazioni);
}
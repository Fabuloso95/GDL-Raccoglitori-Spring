package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.RuoloDTO;
import com.gdl_raccoglitori.dto.response.UtenteResponse;
import com.gdl_raccoglitori.model.Ruolo;
import com.gdl_raccoglitori.model.Utente;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface UtenteMapper 
{
	UtenteMapper INSTANCE = Mappers.getMapper(UtenteMapper.class);
	
    default RuoloDTO toRuoloDTO(Ruolo ruolo) 
    {
        if (ruolo == null) 
        {
            return null;
        }
        
        Long id;
        switch (ruolo) 
        {
            case USER:
                id = 1L;
                break;
            case ADMIN:
                id = 2L;
                break;
            default:
                id = 99L; 
        }
        
        return new RuoloDTO(id, ruolo.name());
    }
    
	UtenteResponse toResponse(Utente utente);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataRegistrazione", ignore = true)
	@Mapping(target = "attivo", ignore = true)
	
	@Mapping(target = "password", ignore = true) 
	@Mapping(target = "resetPasswordToken", ignore = true)
	@Mapping(target = "resetPasswordTokenExpiry", ignore = true)
	@Mapping(target = "refreshToken", ignore = true)
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "providerId", ignore = true)
	
	@Mapping(target = "messaggiInviati", ignore = true)
	@Mapping(target = "messaggiRicevuti", ignore = true)
	@Mapping(target = "lettureCorrenti", ignore = true)
	@Mapping(target = "votiUtente", ignore = true)
	@Mapping(target = "commentiPagina", ignore = true)
	@Mapping(target = "curiositaCreate", ignore = true)
	@Mapping(target = "frasiPreferite", ignore = true)
	Utente toEntity(UtenteRequest utenteRequest);
	
	@Mapping(target = "password", ignore = true) 
    UtenteRequest toUtenteRequest(UtenteUpdateRequest updateRequest);
}

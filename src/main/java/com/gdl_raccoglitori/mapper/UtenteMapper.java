package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.UtenteResponse;
import com.gdl_raccoglitori.model.Utente;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface UtenteMapper 
{
    // Rende l'implementazione del mapper disponibile come Spring Bean e la crea
	UtenteMapper INSTANCE = Mappers.getMapper(UtenteMapper.class);
	
	// =========================================================================
	// 1. Mappatura da ENTITY a RESPONSE (Dati da restituire al client)
	// =========================================================================
	
	/**
	 * Converte un Utente Entity in un UtenteResponse DTO.
	 * I campi con lo stesso nome (es. id, username, email) vengono mappati automaticamente.
	 */
	UtenteResponse toResponse(Utente utente);
	
	// =========================================================================
	// 2. Mappatura da REQUEST a ENTITY (Dati ricevuti dal client da salvare)
	// =========================================================================
	
	/**
	 * Converte un UtenteRequest DTO in un Utente Entity.
	 * * Usiamo l'annotazione @Mapping per ignorare i campi che DEVONO essere 
	 * gestiti in modo sicuro dal Service (non possono essere impostati dal client).
	 */
	
	// Campi generati o impostati dal Service/DB
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataRegistrazione", ignore = true)
	@Mapping(target = "attivo", ignore = true)
	
	// Campi di Sicurezza (Devono essere gestiti dal Service, es. cifratura password)
	@Mapping(target = "password", ignore = true) 
	@Mapping(target = "resetPasswordToken", ignore = true)
	@Mapping(target = "resetPasswordTokenExpiry", ignore = true)
	@Mapping(target = "refreshToken", ignore = true)
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "providerId", ignore = true)
	
	// Relazioni OneToMany (non fanno parte della creazione base)
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
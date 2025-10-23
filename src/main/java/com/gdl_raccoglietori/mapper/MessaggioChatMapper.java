package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglietori.dto.request.MessaggioChatRequest;
import com.gdl_raccoglietori.dto.response.MessaggioChatResponse;
import com.gdl_raccoglietori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AutoreMapper.class})
public interface MessaggioChatMapper 
{
	MessaggioChatResponse toResponse(MessaggioChat messaggioChat);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "mittente", ignore = true)
	@Mapping(target = "dataInvio", ignore = true)
	@Mapping(source = "destinatarioId", target = "destinatario")
	MessaggioChat toEntity(MessaggioChatRequest request);
	
	default Utente toUtente(Long id)
	{
		if(id == null) return null;
		Utente utente = new Utente();
		utente.setId(id);
		return utente;
	}
}

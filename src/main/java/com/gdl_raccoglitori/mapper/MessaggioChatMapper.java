package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglitori.dto.request.MessaggioChatRequest;
import com.gdl_raccoglitori.dto.response.MessaggioChatResponse;
import com.gdl_raccoglitori.model.*;

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

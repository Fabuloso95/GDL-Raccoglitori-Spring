package com.gdl_raccoglietori.mapper;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import org.mapstruct.*;
import com.gdl_raccoglietori.dto.request.VotoUtenteRequest;
import com.gdl_raccoglietori.dto.response.VotoUtenteResponse;
import com.gdl_raccoglietori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VotoUtenteMapper 
{
	DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
	
	@Mapping(source = "utente.id", target = "utenteId")
	@Mapping(source = "propostaVoto.id", target = "propostaVotoId")
	VotoUtenteResponse toResponse(VotoUtente votoUtente);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "utente", ignore = true)
	@Mapping(source = "meseVotazione", target = "meseVotazione")
	@Mapping(source = "propostaVotoId", target = "propostaVoto")
	VotoUtente toEntity(VotoUtenteRequest request);
	
	default YearMonth stringToYearMonth(String mese)
	{
        if (mese == null) return null;
        return YearMonth.parse(mese, FORMATTER);
    }
	
	default PropostaVoto toPropostaVoto(Long id)
	{
        if (id == null) return null;
        PropostaVoto propostaVoto = new PropostaVoto();
        propostaVoto.setId(id);
        return propostaVoto;
    }
	
	default Utente toUtente(Long id)
	{
        if (id == null) return null;
        Utente utente = new Utente();
        utente.setId(id);
        return utente;
    }
}

package com.gdl_raccoglietori.mapper;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import org.mapstruct.*;
import com.gdl_raccoglietori.dto.request.*;
import com.gdl_raccoglietori.dto.response.PropostaVotoResponse;
import com.gdl_raccoglietori.model.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {LibroMapper.class})
public interface PropostaVotoMapper 
{
	DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
	
	PropostaVotoResponse toResponse(PropostaVoto propostaVoto);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataCreazione", ignore = true)
	@Mapping(target = "numVoti", ignore = true)
	@Mapping(target = "votiRicevuti", ignore = true)
	@Mapping(source = "libroId", target = "libroProposto")
	@Mapping(source = "meseVotazione", target = "meseVotazione")
	PropostaVoto toEntity(PropostaVotoRequest request);
	
	default YearMonth stringToYearMonth(String mese)
	{
		if(mese == null) return null;
		return YearMonth.parse(mese, FORMATTER);
	}
	
	default Libro toLibro(Long id)
	{
		if (id == null) return null;
		Libro libro = new Libro();
		libro.setId(id);
		return libro;
	}
}

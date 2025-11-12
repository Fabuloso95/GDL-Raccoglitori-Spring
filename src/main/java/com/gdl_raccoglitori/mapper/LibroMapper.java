package com.gdl_raccoglitori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglitori.dto.request.LibroRequest;
import com.gdl_raccoglitori.dto.response.LibroResponse;
import com.gdl_raccoglitori.model.Libro;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibroMapper
{
	@Mapping(source = "annoPubblicazione", target = "annoPubblicazione")
    @Mapping(source = "numeroPagine", target = "numeroPagine") 
    @Mapping(source = "letto", target = "letto")
	LibroResponse toResponse(Libro libro);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "letto", constant = "false")
	@Mapping(target = "proposteVoto", ignore = true)
	@Mapping(target = "lettureCorrenti", ignore = true)
	@Mapping(target = "curiosita", ignore = true)
	Libro toEntity(LibroRequest libroRequest);
	
	@Mapping(target = "id", ignore = true)
    @Mapping(target = "letto", ignore = true)
    @Mapping(target = "proposteVoto", ignore = true)
    @Mapping(target = "lettureCorrenti", ignore = true)
    @Mapping(target = "curiosita", ignore = true)
    void updateLibroFromRequest(LibroRequest request, @MappingTarget Libro libro);
}

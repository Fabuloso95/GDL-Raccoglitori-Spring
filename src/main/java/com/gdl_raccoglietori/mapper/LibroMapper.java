package com.gdl_raccoglietori.mapper;

import org.mapstruct.*;
import com.gdl_raccoglietori.dto.request.LibroRequest;
import com.gdl_raccoglietori.dto.response.LibroResponse;
import com.gdl_raccoglietori.model.Libro;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibroMapper
{
	LibroResponse toResponse(Libro libro);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "letto", ignore = true)
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

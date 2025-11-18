package com.gdl_raccoglitori.mapper;

import com.gdl_raccoglitori.dto.request.EventoRequestDTO;
import com.gdl_raccoglitori.dto.response.EventoResponseDTO;
import com.gdl_raccoglitori.model.Evento;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventoMapper 
{
    EventoMapper INSTANCE = Mappers.getMapper(EventoMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatoDa", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Evento toEntity(EventoRequestDTO eventoRequestDTO);
    
    @Mapping(source = "creatoDa.username", target = "creatoDaUsername")
    @Mapping(target = "coloreEvento", expression = "java(evento.getTipoEvento() != null ? getColoreEvento(evento.getTipoEvento()) : \"#95A5A6\")")
    EventoResponseDTO toResponseDTO(Evento evento);
    
    default String getColoreEvento(com.gdl_raccoglitori.model.TipoEvento tipoEvento) 
    {
        return switch (tipoEvento) 
        {
            case VOTAZIONE -> "#FF6B6B";
            case DISCUSSIONE -> "#4ECDC4";
            case INCONTRO -> "#45B7D1";
            case SCADENZA -> "#FFA07A";
            default -> "#95A5A6";
        };
    }
}
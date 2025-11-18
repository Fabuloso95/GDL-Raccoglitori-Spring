package com.gdl_raccoglitori.facade;

import java.time.LocalDateTime;
import java.util.List;
import com.gdl_raccoglitori.dto.request.EventoRequestDTO;
import com.gdl_raccoglitori.dto.response.EventoResponseDTO;

public interface EventoFacade 
{
	EventoResponseDTO creaEvento(EventoRequestDTO eventoRequestDTO, String username);
	List<EventoResponseDTO> getEventiNelPeriodo(LocalDateTime inizio, LocalDateTime fine);
	EventoResponseDTO getProssimaVotazione();
	EventoResponseDTO getProssimaDiscussione();
	EventoResponseDTO getEventoById(Long id);
	EventoResponseDTO aggiornaEvento(Long id, EventoRequestDTO eventoRequestDTO);
	void eliminaEvento(Long id);
}

package com.gdl_raccoglitori.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class CommentoPaginaResponse 
{
	private Long id;
	private UtenteResponse utente;
	private Long letturaCorrenteId;
	private Integer paginaRiferimento;
	private String contenuto;
	private LocalDateTime dataCreazione;
}

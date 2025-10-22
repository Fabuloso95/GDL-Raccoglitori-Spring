package com.gdl_raccoglietori.dto.response;

import java.time.LocalDateTime;
import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class CommentoPaginaResponse 
{
	private Long id;
	private Utente utente;
	private LetturaCorrente letturaCorrente;
	private Integer paginaRiferimento;
	private String contenuto;
	private LocalDateTime dataCreazione;
}

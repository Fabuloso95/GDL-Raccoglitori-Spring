package com.gdl_raccoglietori.dto.response;

import java.time.LocalDate;
import java.util.Set;
import lombok.*;

@Data
@Builder
public class LetturaCorrenteResponse
{
	private Long id;
	private UtenteResponse utente;
	private LibroResponse libro;
	private Integer paginaCorrente;
	private LocalDate dataInizio;
	private LocalDate dataCompletamento;
	private Boolean partecipaChiamataZoom;
	private Set<CommentoPaginaResponse> commentiPagina;
}

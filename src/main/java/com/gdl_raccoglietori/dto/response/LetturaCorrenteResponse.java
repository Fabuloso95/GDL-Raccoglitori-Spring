package com.gdl_raccoglietori.dto.response;

import java.time.LocalDate;
import java.util.Set;
import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class LetturaCorrenteResponse
{
	private Long id;
	private Utente utente;
	private Libro libro;
	private Integer paginaCorrente;
	private LocalDate dataInizio;
	private LocalDate dataCompletamento;
	private Boolean partecipaChiamataZoom;
	private Set<CommentoPagina> commentiPagina;
}

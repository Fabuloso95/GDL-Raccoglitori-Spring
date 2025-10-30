package com.gdl_raccoglitori.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Data
@Builder
public class LetturaCorrenteResponse
{
	private Long id;
	private Long utenteId;
	private Long libroId;
	private Integer paginaCorrente;
	private LocalDate dataInizio;
	private LocalDate dataCompletamento;
	private Boolean partecipaChiamataZoom;
	private List<CommentoPaginaResponse> commentiPagina;
}

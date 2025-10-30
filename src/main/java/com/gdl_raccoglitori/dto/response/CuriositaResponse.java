package com.gdl_raccoglitori.dto.response;

import lombok.*;

@Data
@Builder
public class CuriositaResponse 
{
	private Long id;
	private Long libroId;
	private String titolo;
	private String contenuto;
	private Integer paginaRiferimento;
	private AutoreCommentoResponse utenteCreatore;
}

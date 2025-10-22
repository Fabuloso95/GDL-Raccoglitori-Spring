package com.gdl_raccoglietori.dto.response;

import lombok.*;

@Data
@Builder
public class CuriositaResponse 
{
	private Long id;
	private LibroResponse libro;
	private String titolo;
	private String contenuto;
	private Integer paginaRiferimento;
	private UtenteResponse utenteCreatore;
}

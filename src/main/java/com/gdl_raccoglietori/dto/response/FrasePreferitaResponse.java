package com.gdl_raccoglietori.dto.response;

import lombok.*;

@Data
@Builder
public class FrasePreferitaResponse 
{
	private Long id;
	private Long utenteId;
	private Long libroId;
	private String testoFrase;
	private Integer paginaRiferimento;
}

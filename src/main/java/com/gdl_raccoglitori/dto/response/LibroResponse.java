package com.gdl_raccoglitori.dto.response;

import lombok.*;

@Data
@Builder
public class LibroResponse
{
	private Long Id;
	private String titolo;
	private String autore;
	private String copertinaUrl;
	private String sinossi;
	private Integer annoPubblicazione;
	private Integer numeroPagine;
	private Boolean letto;
}

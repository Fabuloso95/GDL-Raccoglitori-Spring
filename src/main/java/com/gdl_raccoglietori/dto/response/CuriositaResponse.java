package com.gdl_raccoglietori.dto.response;

import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class CuriositaResponse 
{
	private Long id;
	private Libro libro;
	private String titolo;
	private String contenuto;
	private Integer paginaRiferimento;
	private Utente utenteCreatore;
}

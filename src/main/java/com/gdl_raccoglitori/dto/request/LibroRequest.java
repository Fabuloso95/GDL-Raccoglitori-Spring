package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LibroRequest
{
	@NotBlank(message = "il titolo è obbligatorio")
	private String titolo;
	
	@NotBlank(message = "l'autore è obbligatorio")
	private String autore;
	
	private String copertinaUrl;
	
	@NotBlank(message = "la sinossi è obbligatoria")
	private String sinossi;
	
	@Positive(message = "l'anno di pubblicazione deve essere un numero positivo")
	@NotNull(message = "l'anno di pubblicazione è obbligatorio")
	private Integer annoPubblicazione;
	
	@Positive(message = "il numero di pagine deve essere positivo")
	@NotNull(message = "il numero di pagine è obbligatorio")
	private Integer numeroPagine;
}

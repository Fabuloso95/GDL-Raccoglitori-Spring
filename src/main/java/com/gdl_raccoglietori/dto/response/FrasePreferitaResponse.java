package com.gdl_raccoglietori.dto.response;

import java.time.LocalDate;
import lombok.*;

@Data
@Builder
public class FrasePreferitaResponse 
{
	private Long id;
	private UtenteResponse utente;
	private LibroResponse libro;
	private String testoFrase;
	private Integer paginaRiferimento;
	private LocalDate dataInizio;
}

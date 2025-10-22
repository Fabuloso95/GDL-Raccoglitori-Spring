package com.gdl_raccoglietori.dto.response;

import java.time.LocalDate;
import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class FrasePreferitaResponse 
{
	private Long id;
	private Utente utente;
	private Libro libro;
	private String testoFrase;
	private Integer paginaRiferimento;
	private LocalDate dataInizio;
}

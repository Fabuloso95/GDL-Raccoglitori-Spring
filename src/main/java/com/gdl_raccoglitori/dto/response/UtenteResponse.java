package com.gdl_raccoglitori.dto.response;

import java.time.LocalDate;
import lombok.*;

@Data
@Builder
public class UtenteResponse 
{
	private Long id;
	private String username;
	private String email;
	private String nome;
	private String cognome;
	private RuoloDTO ruolo;
	private LocalDate dataRegistrazione;
	private Boolean attivo;
}

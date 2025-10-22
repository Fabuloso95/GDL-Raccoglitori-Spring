package com.gdl_raccoglietori.dto.response;

import java.time.LocalDate;
import com.gdl_raccoglietori.model.Ruolo;
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
	private Ruolo ruolo;
	private LocalDate dataRegistrazione;
	private Boolean attivo;
}

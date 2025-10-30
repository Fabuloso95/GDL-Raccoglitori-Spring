package com.gdl_raccoglitori.dto.request;

import com.gdl_raccoglitori.model.Ruolo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UtenteRequest 
{
		@NotBlank(message = "l'username è obbligatorio")
		private String username;
		
		@NotBlank(message = "il nome è obbligatorio")
		private String nome;
		
		@NotBlank(message = "il cognome è obbligatorio")
		private String cognome;
		
		private String password;
		
		@Email(message = "Formato email non valido")
		@NotBlank(message = "l'email è obbligatoria")
		private String email;
		
		private Ruolo ruolo;
}

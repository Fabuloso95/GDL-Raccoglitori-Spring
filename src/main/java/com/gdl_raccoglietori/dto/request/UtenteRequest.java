package com.gdl_raccoglietori.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UtenteRequest 
{
	@NotBlank(message = "l'username è obbligatorio")
	private String username;
	
	
}

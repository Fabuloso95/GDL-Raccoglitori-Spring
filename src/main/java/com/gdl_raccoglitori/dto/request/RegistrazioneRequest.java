package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrazioneRequest 
{
	@NotBlank(message = "L'username è obbligatorio")
	@Size(min = 3, max = 50, message = "l'username deve contenere minimo 3 caratteri e massimo 50")
	private String username;
	
	@Email(message = "formato email non valido")
	@NotBlank(message = "l'email è obbligatoria")
	private String email;
	
	@NotBlank(message = "La Password è obbligatoria")
    @Size(min = 8, message = "La password deve essere lunga almeno 8 caratteri")
	@Pattern(
			    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
			    message = "La password deve contenere almeno una maiuscola, una minuscola, un numero e un carattere speciale")
    private String password;
	
	@NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    @NotBlank(message = "Il cognome non può essere vuoto")
    private String cognome;
}

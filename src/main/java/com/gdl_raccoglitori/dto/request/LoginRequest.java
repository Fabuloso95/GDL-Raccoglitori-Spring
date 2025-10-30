package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest 
{
	@NotBlank(message = "Username o Email non può essere vuoto")
    private String usernameOrEmail; // L'utente può usare l'username o l'email per il login

    @NotBlank(message = "La password non può essere vuota")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
    message = "La password deve contenere almeno 8 caratteri, inclusi una lettera maiuscola, una minuscola, un numero e un carattere speciale (@$!%*?&)")
    private String password;
}

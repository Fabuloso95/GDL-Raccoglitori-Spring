package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import com.gdl_raccoglitori.model.Ruolo;

@Data
public class UtenteUpdateRequest 
{
    private String username;
    private String nome;
    private String cognome;
    
    @Email(message = "Formato email non valido")
    private String email;
    
    private Ruolo ruolo;

    @Pattern(regexp = "^\\+?[0-9\\s]{6,20}$", message = "Telefono non valido")
    private String telefono;

    private LocalDate dataNascita;
}

package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PropostaVotoRequest
{
    // ID del Libro che viene proposto per la votazione
    @NotNull(message = "L'ID del libro proposto è obbligatorio")
    private Long libroId;

    // Mese e Anno della votazione (es: 2025-10). Useremo una stringa per semplificare l'input.
    @NotBlank(message = "Il mese e l'anno di votazione sono obbligatori")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Il formato del mese/anno deve essere YYYY-MM")
    private String meseVotazione; 
}

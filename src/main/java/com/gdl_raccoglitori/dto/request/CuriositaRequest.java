package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CuriositaRequest 
{
    @NotNull(message = "L'ID del libro è obbligatorio")
    private Long libroId;
    
    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(max = 100, message = "Il titolo non può superare i 100 caratteri")
    private String titolo;
    
    @NotBlank(message = "Il contenuto è obbligatorio")
    private String contenuto;
    
    @PositiveOrZero(message = "La pagina di riferimento deve essere zero o positiva")
    private Integer paginaRiferimento; // Può essere null se si riferisce all'intero libro
}

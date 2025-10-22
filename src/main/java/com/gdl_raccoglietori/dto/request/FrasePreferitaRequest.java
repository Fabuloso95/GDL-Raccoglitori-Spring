package com.gdl_raccoglietori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FrasePreferitaRequest 
{
    @NotNull(message = "L'ID del libro è obbligatorio")
    private Long libroId;
    
    @NotBlank(message = "Il testo della frase non può essere vuoto")
    private String testoFrase;
    
    @PositiveOrZero(message = "La pagina di riferimento deve essere zero o positiva")
    private Integer paginaRiferimento;
}

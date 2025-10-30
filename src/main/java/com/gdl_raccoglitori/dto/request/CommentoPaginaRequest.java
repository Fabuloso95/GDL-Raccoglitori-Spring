package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CommentoPaginaRequest 
{
    // ID della LetturaCorrente a cui il commento si riferisce
    @NotNull(message = "L'ID della lettura corrente è obbligatorio")
    private Long letturaCorrenteId;
    
    @NotNull(message = "La pagina di riferimento è obbligatoria")
    @Positive(message = "La pagina di riferimento deve essere positiva")
    private Integer paginaRiferimento;
    
    @NotBlank(message = "Il contenuto del commento non può essere vuoto")
    private String contenuto;
}

package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AggiornaPaginaRequest
{
    // La nuova pagina corrente in cui si trova l'utente
    @PositiveOrZero(message = "Il numero di pagina deve essere zero o positivo")
    private Integer paginaCorrente;
    
    // Il flag per la partecipazione alla call, aggiornabile separatamente
    private Boolean partecipaChiamataZoom; 
}

// EventoResponseDTO.java
package com.gdl_raccoglitori.dto.response;

import com.gdl_raccoglitori.model.TipoEvento;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoResponseDTO 
{
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private TipoEvento tipoEvento;
    private String creatoDaUsername;
    private String coloreEvento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public String getColoreEvento() 
    {
        return switch (tipoEvento) 
        {
            case VOTAZIONE -> "#FF6B6B";
            case DISCUSSIONE -> "#4ECDC4";
            case INCONTRO -> "#45B7D1";
            case SCADENZA -> "#FFA07A";
            default -> "#95A5A6";
        };
    }
}
package com.gdl_raccoglitori.dto.request;

import com.gdl_raccoglitori.model.TipoEvento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoRequestDTO 
{
	@NotNull(message = "il titolo è obbligatorio")
    private String titolo;
    private String descrizione;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private TipoEvento tipoEvento;
}
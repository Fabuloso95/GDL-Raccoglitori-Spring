package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VotoUtenteRequest 
{
    // ID della PropostaVoto a cui si riferisce il voto
    @NotNull(message = "L'ID della proposta di voto è obbligatorio")
    private Long propostaVotoId;
    
    // Il mese della votazione per verifica lato server
    @NotBlank(message = "Il mese e l'anno di votazione sono obbligatori")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Il formato del mese/anno deve essere YYYY-MM")
    private String meseVotazione;
}

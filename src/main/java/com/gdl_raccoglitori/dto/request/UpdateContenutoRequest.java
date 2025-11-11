package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class UpdateContenutoRequest 
{
    
    @NotBlank(message = "Il contenuto del commento non può essere vuoto.")
    private String nuovoContenuto;
}

package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ImpostazioniRequest 
{
    
    private Boolean notificheEmail;
    
    private Boolean notifichePush;
    
    @Pattern(regexp = "^(it|en)$", message = "Lingua deve essere 'it' o 'en'")
    private String lingua;
    
    @Pattern(regexp = "^(system|light|dark)$", message = "Tema deve essere 'system', 'light' o 'dark'")
    private String tema;
    
    private Boolean emailRiassuntoSettimanale;
    
    private Boolean privacyProfiloPubblico;
}
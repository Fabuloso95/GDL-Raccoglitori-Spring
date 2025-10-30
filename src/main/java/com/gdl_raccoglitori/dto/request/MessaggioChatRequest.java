package com.gdl_raccoglitori.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
public class MessaggioChatRequest
{
    @NotNull(message = "L'ID del gruppo/chat è obbligatorio")
    private UUID gruppoID; 
    
    // Tipo di chat (es. "PRIVATA", "GRUPPO")
    @NotBlank(message = "Il tipo di chat è obbligatorio")
    private String tipoChat; 
    
    // ID del destinatario se il tipoChat è "PRIVATA". Può essere null.
    private Long destinatarioId;
    
    @NotBlank(message = "Il contenuto del messaggio è obbligatorio")
    private String contenuto;
}

package com.gdl_raccoglitori.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImpostazioniResponse 
{
    
    private Long id;
    private Long utenteId;
    private Boolean notificheEmail;
    private Boolean notifichePush;
    private String lingua;
    private String tema;
    private Boolean emailRiassuntoSettimanale;
    private Boolean privacyProfiloPubblico;
    private LocalDateTime dataAggiornamento;
}
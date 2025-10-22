package com.gdl_raccoglietori.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Data
@Builder
public class MessaggioChatResponse 
{
	private Long id;
	private UtenteResponse mittente;
	private UtenteResponse destinatario;
	private UUID gruppoID;
	private String tipoChat;
	private String contenuto;
	private LocalDateTime dataInvio;
}

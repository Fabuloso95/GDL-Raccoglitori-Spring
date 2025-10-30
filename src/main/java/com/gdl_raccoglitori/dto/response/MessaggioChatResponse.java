package com.gdl_raccoglitori.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Data
@Builder
public class MessaggioChatResponse 
{
	private Long id;
	private AutoreCommentoResponse mittente;
	private AutoreCommentoResponse destinatario;
	private UUID gruppoID;
	private String tipoChat;
	private String contenuto;
	private LocalDateTime dataInvio;
}

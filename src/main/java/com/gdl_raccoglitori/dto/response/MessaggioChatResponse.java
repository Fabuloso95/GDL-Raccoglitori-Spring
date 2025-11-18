package com.gdl_raccoglitori.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class MessaggioChatResponse 
{
	private Long id;
	private AutoreCommentoResponse mittente;
	private AutoreCommentoResponse destinatario;
	private String gruppoID;
	private String tipoChat;
	private String contenuto;
	private LocalDateTime dataInvio;
}

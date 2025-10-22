package com.gdl_raccoglietori.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class MessaggioChatResponse 
{
	private Long id;
	private Utente mittente;
	private Utente destinatario;
	private UUID gruppoID;
	private String tipoChat;
	private String contenuto;
	private LocalDateTime dataInvio;
}

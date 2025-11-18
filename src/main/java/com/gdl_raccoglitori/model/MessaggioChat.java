package com.gdl_raccoglitori.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "messaggi_chat")
public class MessaggioChat 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mittente_id", nullable = false)
	private Utente mittente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destinatario_id") // Destinatario è opzionale (per chat di gruppo)
	private Utente destinatario;
	
	@Column(name = "gruppo_id")
	private String gruppoID; // ID del gruppo (es. ID del Libro corrente o della chat privata)
	
	@Column(name = "tipo_chat", nullable = false)
	private String tipoChat; // Esempio: "PRIVATA", "GRUPPO"
	
	@Lob
	@NotBlank(message = "Il contenuto del messaggio è obbligatorio")
	private String contenuto;
	
	@PastOrPresent(message = "La data di invio non può essere futura")
	@Column(name = "data_invio")
	private LocalDateTime dataInvio;
}

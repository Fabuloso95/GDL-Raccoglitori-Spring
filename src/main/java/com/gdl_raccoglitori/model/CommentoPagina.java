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
@Table(name = "commenti_pagina")
public class CommentoPagina 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lettura_corrente_id", nullable = false)
	private LetturaCorrente letturaCorrente;
	
	@PositiveOrZero(message = "La pagina di riferimento non può essere negativa")
	@Column(nullable = false,name = "pagina_di_riferimento")
	private Integer paginaRiferimento;
	
	@Lob
	@NotBlank(message = "Il contenuto del commento è obbligatorio")
	private String contenuto;
	
	@PastOrPresent(message = "La data di creazione non può essere futura")
	@Column(name = "data_creazione")
	private LocalDateTime dataCreazione;
}

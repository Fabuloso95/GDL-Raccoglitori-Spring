package com.gdl_raccoglietori.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Libro 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "il titolo è obbligatorio")
	@Column(length = 80, nullable = false, unique = true)
	private String titolo;
	
	@NotBlank(message = "l'autore è obbligatorio")
	@Column(nullable = false, length = 80)
	private String autore;
	
	private String copertinaUrl;
	
	@Lob
	private String sinossi;
	
	@Positive
	@Column(nullable = false)
	private Integer annoPubblicazione;
	
	@Positive
	@Column(nullable = false)
	private Integer numeroPagine;
	
	@Column(nullable = false)
	private Boolean letto;
}

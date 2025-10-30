package com.gdl_raccoglitori.model;

import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"proposteVoto", "lettureCorrenti", "curiosita"})
@EqualsAndHashCode(exclude = {"proposteVoto", "lettureCorrenti", "curiosita"})
public class Libro 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Il titolo è obbligatorio")
	@Column(length = 80, nullable = false, unique = true)
	private String titolo;
	
	@NotBlank(message = "L'autore è obbligatorio")
	@Column(nullable = false, length = 80)
	private String autore;
	
	@Column(name = "copertina_url")
	private String copertinaUrl;
	
	@Lob
	private String sinossi;
	
	@Positive(message = "L'anno di pubblicazione deve essere un numero positivo")
	@Column(nullable = false, name = "anno_pubblicazione")
	private Integer annoPubblicazione;
	
	@Positive(message = "Il numero di pagine deve essere un numero positivo")
	@Column(nullable = false, name = "numero_pagine")
	private Integer numeroPagine;
	
	@Column(nullable = false)
	private Boolean letto;

    @OneToMany(mappedBy = "libroProposto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PropostaVoto> proposteVoto;
    
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LetturaCorrente> lettureCorrenti;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Curiosita> curiosita;
}

package com.gdl_raccoglitori.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Curiosita 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libro_id", nullable = false)
	private Libro libro;
	
	@Column(nullable = false)
	@NotBlank(message = "Il titolo è obbligatorio")
	private String titolo;
	
	@Lob
	@NotBlank(message = "Il contenuto è obbligatorio")
	private String contenuto;
	
	// Riferimento opzionale alla pagina, non obbligatorio in questo contesto
	@Column(name = "pagina_di_riferimento")
	private Integer paginaRiferimento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_creatore_id", nullable = false)
	private Utente utenteCreatore;
}

package com.gdl_raccoglitori.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "frasi_preferite")
public class FrasePreferita 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Relazione con l'utente che salva la frase
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utente;
	
	// Relazione con il libro da cui proviene la frase
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libro_id", nullable = false)
	private Libro libro;
	
	@Lob
	@NotBlank(message = "Il testo della frase non può essere vuoto")
	private String testoFrase;
	
	// La pagina esatta dove si trova la frase (opzionale)
	@Column(name = "pagina_riferimento")
	@PositiveOrZero(message = "La pagina deve essere un numero positivo")
	private Integer paginaRiferimento;
}

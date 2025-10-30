package com.gdl_raccoglitori.model;

import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"commentiPagina"})
@EqualsAndHashCode(exclude = {"commentiPagina"})
@Table(name = "lettura_corrente")
public class LetturaCorrente 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libro_id", nullable = false)
	private Libro libro;
	
	@PositiveOrZero(message = "La pagina corrente non può essere negativa")
	@Column(nullable = false, name = "pagina_corrente")
	private Integer paginaCorrente;
	
	@Column(name = "data_inizio", nullable = false)
	@PastOrPresent
	private LocalDate dataInizio;
	
	@Column(name = "data_completamento") // Può essere NULL se non finito
	private LocalDate dataCompletamento;
	
	@Column(name = "partecipa_chiamata_zoom")
	private Boolean partecipaChiamataZoom;
	
    @OneToMany(mappedBy = "letturaCorrente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentoPagina> commentiPagina;
}

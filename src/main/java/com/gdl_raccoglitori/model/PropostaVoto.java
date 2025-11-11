package com.gdl_raccoglitori.model;

import java.time.*;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"votiRicevuti"})
@EqualsAndHashCode(exclude = {"votiRicevuti"})
public class PropostaVoto 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libro_proposto_id", nullable = false)
	private Libro libroProposto;
	
	@Column(nullable = false, name = "mese_votazione", columnDefinition = "TEXT")
	private String meseVotazione;
	
	@Column(nullable = false, name = "data_creazione")
	@PastOrPresent(message = "La data di creazione non può essere futura")
	private LocalDateTime dataCreazione;
	
	@Column(nullable = false, name = "numero_voti")
	@PositiveOrZero(message = "Il numero di voti non può essere negativo")
	private Integer numVoti;
	
    @OneToMany(mappedBy = "propostaVoto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VotoUtente> votiRicevuti;
}

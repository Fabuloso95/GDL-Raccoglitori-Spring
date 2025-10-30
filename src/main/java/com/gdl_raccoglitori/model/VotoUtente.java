package com.gdl_raccoglitori.model;

import java.time.YearMonth;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class VotoUtente
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proposta_voto_id", nullable = false)
	private PropostaVoto propostaVoto;
	
	@Column(name = "mese_votazione", columnDefinition = "TEXT")
	private YearMonth meseVotazione;
}

package com.gdl_raccoglietori.dto.response;

import java.time.*;
import lombok.*;

@Data
@Builder
public class VotoUtenteResponse 
{
	    private Long id;
		private UtenteResponse utente;
		private PropostaVotoResponse propostaVoto;
		private YearMonth meseVotazione;
}
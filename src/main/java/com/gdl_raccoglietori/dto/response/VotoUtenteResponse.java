package com.gdl_raccoglietori.dto.response;

import java.time.*;
import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class VotoUtenteResponse 
{
	    private Long id;
		private Utente utente;
		private PropostaVoto propostaVoto;
		private YearMonth meseVotazione;
}
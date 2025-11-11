package com.gdl_raccoglitori.dto.response;

import lombok.*;

@Data
@Builder
public class VotoUtenteResponse 
{
	    private Long id;
	    private Long utenteId;
		private Long propostaVotoId;
		private String meseVotazione;
}
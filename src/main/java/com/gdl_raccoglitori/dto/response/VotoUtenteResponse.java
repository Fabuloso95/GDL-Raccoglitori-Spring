package com.gdl_raccoglitori.dto.response;

import java.time.*;
import lombok.*;

@Data
@Builder
public class VotoUtenteResponse 
{
	    private Long id;
	    private Long utenteId;
		private Long propostaVotoId;
		private YearMonth meseVotazione;
}
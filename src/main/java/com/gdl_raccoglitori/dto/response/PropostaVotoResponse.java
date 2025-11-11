package com.gdl_raccoglitori.dto.response;

import java.time.*;
import lombok.*;

@Data
@Builder
public class PropostaVotoResponse 
{
	private Long id;
	private LibroResponse libroProposto; 
	private String meseVotazione;
	private LocalDateTime dataCreazione;
	private Integer numVoti;
}

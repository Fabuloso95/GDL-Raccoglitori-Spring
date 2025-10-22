package com.gdl_raccoglietori.dto.response;

import java.time.*;
import java.util.Set;
import com.gdl_raccoglietori.model.*;
import lombok.*;

@Data
@Builder
public class PropostaVotoResponse 
{
	private Long id;
	private Libro libroProposto;
	private YearMonth meseVotazione;
	private LocalDateTime dataCreazione;
	private Integer numVoti;
    private Set<VotoUtente> votiRicevuti;
}

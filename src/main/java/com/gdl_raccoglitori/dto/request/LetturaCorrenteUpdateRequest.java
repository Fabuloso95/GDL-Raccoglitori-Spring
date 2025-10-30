package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LetturaCorrenteUpdateRequest 
{
	@PositiveOrZero(message = "La pagina corrente non può essere negativa")
    @NotNull(message = "Il campo paginaCorrente è obbligatorio")
	private Integer paginaCorrente;
	private Boolean partecipaChiamataZoom;
}

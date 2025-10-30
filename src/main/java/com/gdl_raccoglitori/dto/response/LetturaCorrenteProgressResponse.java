package com.gdl_raccoglitori.dto.response;

import lombok.*;

@Data
@Builder
public class LetturaCorrenteProgressResponse 
{
	private Long letturaCorrenteId;
	private String username;
	private Integer paginaCorrente;
	private Boolean partecipaChiamataZoom;
}

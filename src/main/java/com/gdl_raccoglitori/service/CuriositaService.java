package com.gdl_raccoglitori.service;

import java.util.List;
import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.model.Curiosita;
import com.gdl_raccoglitori.model.Utente;

public interface CuriositaService 
{
	Curiosita createCuriosita(CuriositaRequest request, Utente utenteCreatore);
	Curiosita updateCuriosita(Long curiositaId, CuriositaRequest request, Utente utenteRichiedente);
	void deleteCuriosita(Long curiositaId, Utente utenteRichiedente);
	Curiosita findById(Long id);
	List<Curiosita> findByLibroId(Long libroId);
	List<Curiosita> findByLibroAndPagina(Long libroId, Integer paginaRiferimento);
}

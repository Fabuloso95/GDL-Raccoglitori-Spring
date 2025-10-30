package com.gdl_raccoglitori.service;

import java.util.List;
import com.gdl_raccoglitori.dto.request.FrasePreferitaRequest;
import com.gdl_raccoglitori.model.FrasePreferita;
import com.gdl_raccoglitori.model.Utente;

public interface FrasePreferitaService
{
	FrasePreferita saveFrasePreferita(FrasePreferitaRequest request, Utente utenteSalvatore);
	void deleteFrasePreferita(Long fraseId, Utente utenteRichiedente);
	FrasePreferita findById(Long id);
	List<FrasePreferita> findByUtente(Utente utenteSalvatore);
	List<FrasePreferita> findByLibroId(Long libroId);
}

package com.gdl_raccoglitori.service;

import java.util.List;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.model.*;

public interface LetturaCorrenteService
{
	LetturaCorrente startNewReading(LetturaCorrenteRequest request, Utente utente);
	LetturaCorrente updateReadingProgress(Long letturaId, LetturaCorrenteUpdateRequest request, Utente utenteRichiedente);
	LetturaCorrente completeReading(Long letturaId, Utente utenteRichiedente);
	void deleteReading(Long letturaId, Utente utenteRichiedente);   
	LetturaCorrente findById(Long id);
    List<LetturaCorrente> findByUtente(Utente utente);
    List<Object[]> findUsersProgressByLibroId(Long libroId);
}

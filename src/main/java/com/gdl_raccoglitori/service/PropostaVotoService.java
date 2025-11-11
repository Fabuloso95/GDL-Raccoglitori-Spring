package com.gdl_raccoglitori.service;

import java.util.List;
import com.gdl_raccoglitori.dto.request.PropostaVotoRequest;
import com.gdl_raccoglitori.model.*;

public interface PropostaVotoService 
{
    PropostaVoto createProposta(PropostaVotoRequest request);
    PropostaVoto findWinnerProposta(String meseVotazione);
    List<PropostaVoto> findByMeseVotazione(String meseVotazione);
    VotoUtente registeVoto(Long propostaVotoId, Utente utente, String meseVotazioneStr);
    PropostaVoto findById(Long id);
	PropostaVoto updateProposta(Long id, PropostaVotoRequest request, Utente utenteCorrente);
	void deleteProposta(Long id);
}

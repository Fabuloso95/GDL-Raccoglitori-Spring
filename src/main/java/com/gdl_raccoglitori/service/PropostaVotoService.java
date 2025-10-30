package com.gdl_raccoglitori.service;

import java.time.YearMonth;
import java.util.List;
import com.gdl_raccoglitori.dto.request.PropostaVotoRequest;
import com.gdl_raccoglitori.model.*;

public interface PropostaVotoService 
{
    PropostaVoto createProposta(PropostaVotoRequest request);
    PropostaVoto findWinnerProposta(YearMonth meseVotazione);
    List<PropostaVoto> findByMeseVotazione(YearMonth meseVotazione);
    VotoUtente registeVoto(Long propostaVotoId, Utente utente, String meseVotazioneStr);
    PropostaVoto findById(Long id);
}

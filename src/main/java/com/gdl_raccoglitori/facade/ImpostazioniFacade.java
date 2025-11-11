package com.gdl_raccoglitori.facade;

import com.gdl_raccoglitori.dto.request.ImpostazioniRequest;
import com.gdl_raccoglitori.dto.response.ImpostazioniResponse;

public interface ImpostazioniFacade 
{
    
    ImpostazioniResponse getImpostazioniUtente(Long utenteId);
    
    ImpostazioniResponse createImpostazioniDefault(Long utenteId);
    
    ImpostazioniResponse updateImpostazioni(Long utenteId, ImpostazioniRequest request);
    
    void deleteImpostazioni(Long utenteId);
}
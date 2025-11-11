package com.gdl_raccoglitori.service;

import com.gdl_raccoglitori.dto.request.ImpostazioniRequest;
import com.gdl_raccoglitori.model.Impostazioni;
import java.util.Optional;

public interface ImpostazioniService 
{
    
    Impostazioni getImpostazioniUtente(Long utenteId);
    
    Optional<Impostazioni> getImpostazioniUtenteOptional(Long utenteId);
    
    Impostazioni createImpostazioniDefault(Long utenteId);
    
    Impostazioni updateImpostazioni(Long utenteId, ImpostazioniRequest request);
    
    void deleteImpostazioni(Long utenteId);
    
    boolean existsByUtenteId(Long utenteId);
}
package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.UtenteResponse;
import com.gdl_raccoglitori.model.Ruolo;

public interface UtenteFacade 
{
	List<UtenteResponse> getAll();
    UtenteResponse getById(Long id);
    UtenteResponse create(UtenteRequest request);
    UtenteResponse update(Long id, UtenteUpdateRequest request); 
    void deleteById(Long id);
    List<UtenteResponse> search(String term);
    List<UtenteResponse> findByNomeAndCognome(String nome, String cognome);
    List<UtenteResponse> findByRuolo(Ruolo ruolo);
    UtenteResponse attivaUtente(Long id);
    UtenteResponse disattivaUtente(Long id);
    UtenteResponse cambiaRuolo(Long id, Ruolo nuovoRuolo);
    void forgotPassword(String email);
    void resetPassword(String token, String nuovaPassword);
    UtenteResponse updatePassword(Long id, String nuovaPassword);
}

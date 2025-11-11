package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.request.ImpostazioniRequest;
import com.gdl_raccoglitori.dto.response.ImpostazioniResponse;
import com.gdl_raccoglitori.facade.ImpostazioniFacade;
import com.gdl_raccoglitori.mapper.ImpostazioniMapper;
import com.gdl_raccoglitori.model.Impostazioni;
import com.gdl_raccoglitori.service.ImpostazioniService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImpostazioniFacadeImpl implements ImpostazioniFacade 
{
    private final ImpostazioniService impostazioniService;
    private final ImpostazioniMapper impostazioniMapper;

    @Override
    @Transactional(readOnly = true)
    public ImpostazioniResponse getImpostazioniUtente(Long utenteId) 
    {
        log.debug("Facade: Recupero impostazioni per utente ID: {}", utenteId);
        Impostazioni impostazioni = impostazioniService.getImpostazioniUtente(utenteId);
        return impostazioniMapper.toResponse(impostazioni);
    }

    @Override
    @Transactional
    public ImpostazioniResponse createImpostazioniDefault(Long utenteId) 
    {
        log.info("Facade: Creazione impostazioni default per utente ID: {}", utenteId);
        Impostazioni impostazioni = impostazioniService.createImpostazioniDefault(utenteId);
        return impostazioniMapper.toResponse(impostazioni);
    }

    @Override
    @Transactional
    public ImpostazioniResponse updateImpostazioni(Long utenteId, ImpostazioniRequest request)
    {
        log.info("Facade: Aggiornamento impostazioni per utente ID: {}", utenteId);
        Impostazioni impostazioni = impostazioniService.updateImpostazioni(utenteId, request);
        return impostazioniMapper.toResponse(impostazioni);
    }

    @Override
    @Transactional
    public void deleteImpostazioni(Long utenteId)
    {
        log.warn("Facade: Eliminazione impostazioni per utente ID: {}", utenteId);
        impostazioniService.deleteImpostazioni(utenteId);
    }
}
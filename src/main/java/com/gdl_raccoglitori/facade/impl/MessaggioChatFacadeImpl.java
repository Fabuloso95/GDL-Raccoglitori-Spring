package com.gdl_raccoglitori.facade.impl;

import com.gdl_raccoglitori.dto.request.MessaggioChatRequest;
import com.gdl_raccoglitori.dto.response.MessaggioChatResponse;
import com.gdl_raccoglitori.exceptionhandler.exception.*;
import com.gdl_raccoglitori.facade.MessaggioChatFacade;
import com.gdl_raccoglitori.mapper.MessaggioChatMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.service.MessaggioChatService;
import com.gdl_raccoglitori.repository.UtenteRepository; 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessaggioChatFacadeImpl implements MessaggioChatFacade 
{
    private final MessaggioChatService messaggioChatService;
    private final MessaggioChatMapper messaggioChatMapper;
    private final UtenteRepository utenteRepository;

    private Utente getCurrentAuthenticatedUser() 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof Utente)) 
        {
            log.error("Tentativo di accesso non autorizzato. Nessun utente Utente trovato nel contesto.");
            throw new OperazioneNonAutorizzataException("Utente non autenticato.");
        }
        
        return (Utente) authentication.getPrincipal(); 
    }

    @Override
    public MessaggioChatResponse sendMessage(MessaggioChatRequest request) 
    {
        Utente mittente = getCurrentAuthenticatedUser();
        
        log.debug("Invio messaggio di tipo {} da Utente ID {} per Gruppo ID {}", 
                  request.getTipoChat(), mittente.getId(), request.getGruppoID());
                  
        MessaggioChat messaggio = messaggioChatService.sendNewMessage(request, mittente);
        
        return messaggioChatMapper.toResponse(messaggio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessaggioChatResponse> getGroupChatHistoryByNome(String gruppoID) 
    {
        log.debug("Recupero cronologia chat per Gruppo ID {}", gruppoID);
        
        List<MessaggioChat> messaggiList = messaggioChatService.findGroupChatMessages(gruppoID);
        
        return messaggiList.stream()
                .map(messaggioChatMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessaggioChatResponse> getPrivateChatHistory(Long altroUtenteId) 
    {
        Utente utenteA = getCurrentAuthenticatedUser();
        Utente utenteB = utenteRepository.findById(altroUtenteId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente con ID " + altroUtenteId + " non trovato."));

        log.debug("Recupero cronologia chat privata tra Utente ID {} e Utente ID {}", utenteA.getId(), utenteB.getId());
        
        List<MessaggioChat> messaggiList = messaggioChatService.findPrivateChatMessages(utenteA, utenteB);
        
        return messaggiList.stream()
                .map(messaggioChatMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteMessage(Long messaggioId) 
    {
        Utente richiedente = getCurrentAuthenticatedUser();
        log.warn("Utente ID {} sta tentando di eliminare il messaggio ID {}", richiedente.getId(), messaggioId);
        
        messaggioChatService.deleteMessage(messaggioId, richiedente);
    }
}

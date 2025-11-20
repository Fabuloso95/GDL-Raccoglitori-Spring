package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.dto.request.MessaggioChatRequest;
import com.gdl_raccoglitori.exceptionhandler.exception.*;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.*;
import com.gdl_raccoglitori.service.MessaggioChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessaggioChatServiceImpl implements MessaggioChatService 
{
    private final MessaggioChatRepository messaggioChatRepository;
    private final UtenteRepository utenteRepository;
    
    private static final String TIPO_CHAT_PRIVATA = "PRIVATA";
    private static final String TIPO_CHAT_GRUPPO = "GRUPPO";

    @Override
    public MessaggioChat sendNewMessage(MessaggioChatRequest request, Utente mittente) 
    {
        MessaggioChat messaggio = new MessaggioChat();
        messaggio.setMittente(mittente);
        messaggio.setContenuto(request.getContenuto());
        messaggio.setTipoChat(request.getTipoChat().toUpperCase());
        messaggio.setDataInvio(LocalDateTime.now());

        if (TIPO_CHAT_PRIVATA.equals(messaggio.getTipoChat())) 
        {
            if (request.getDestinatarioId() == null)
            {
                throw new IllegalArgumentException("Per le chat private, l'ID del destinatario è obbligatorio.");
            }
            
            Utente destinatario = utenteRepository.findById(request.getDestinatarioId())
                    .orElseThrow(() -> new RisorsaNonTrovataException("Destinatario con ID " + request.getDestinatarioId() + " non trovato."));
            
            messaggio.setDestinatario(destinatario);
            
            String gruppoId = generaGruppoIdPrivata(mittente.getId(), destinatario.getId());
            messaggio.setGruppoID(gruppoId);
            
            log.info("Messaggio privato inviato da {} a {}", mittente.getUsername(), destinatario.getUsername());
        } 
        else if (TIPO_CHAT_GRUPPO.equals(messaggio.getTipoChat()))
        {
            messaggio.setDestinatario(null);
            
            String gruppoId = request.getGruppoId();
            if (gruppoId == null || gruppoId.trim().isEmpty()) 
            {
                gruppoId = "gruppo_generale";
            }
            messaggio.setGruppoID(gruppoId);
            
            log.info("Messaggio di gruppo inviato da {} al gruppo ID {}", mittente.getUsername(), gruppoId);
        }
        else
        {
            throw new IllegalArgumentException("Tipo di chat non valido: " + request.getTipoChat());
        }

        return messaggioChatRepository.save(messaggio);
    }

    private String generaGruppoIdPrivata(Long utenteId1, Long utenteId2) 
    {
        Long minId = Math.min(utenteId1, utenteId2);
        Long maxId = Math.max(utenteId1, utenteId2);
        return "privata_" + minId + "_" + maxId;
    }

    @Override
    public List<MessaggioChat> findGroupChatMessages(String gruppoID) 
    {
        return messaggioChatRepository.findByGruppoIDOrderByDataInvioAsc(gruppoID);
    }

    @Override
    public List<MessaggioChat> findPrivateChatMessages(Utente utenteA, Utente utenteB) 
    {
        String gruppoId = generaGruppoIdPrivata(utenteA.getId(), utenteB.getId());
        return messaggioChatRepository.findByGruppoIDOrderByDataInvioAsc(gruppoId);
    }

    @Override
    public MessaggioChat findById(Long id) 
    {
        return messaggioChatRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Messaggio chat con ID " + id + " non trovato."));
    }
    
    @Override
    public void deleteMessage(Long messaggioId, Utente utenteRichiedente) 
    {
        MessaggioChat messaggio = findById(messaggioId);

        if (!messaggio.getMittente().getId().equals(utenteRichiedente.getId())) 
        {
            log.warn("Tentativo non autorizzato di eliminare il messaggio ID {} da parte dell'utente ID {}",
                     messaggioId, utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad eliminare questo messaggio. Solo il mittente può farlo.");
        }

        log.info("Eliminazione messaggio ID {} da parte del mittente ID {}", messaggioId, utenteRichiedente.getId());
        messaggioChatRepository.delete(messaggio);
    }
}

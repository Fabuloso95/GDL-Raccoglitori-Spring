package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.MessaggioChatRequest;
import com.gdl_raccoglitori.dto.response.MessaggioChatResponse;
import com.gdl_raccoglitori.facade.MessaggioChatFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/chat") 
@RequiredArgsConstructor
@Slf4j
public class MessaggioChatController 
{
    private final MessaggioChatFacade messaggioChatFacade;

    @PostMapping
    public ResponseEntity<MessaggioChatResponse> sendMessage(@Valid @RequestBody MessaggioChatRequest request) 
    {
        log.info("Controller: Ricevuta richiesta per invio messaggio di tipo {}", request.getTipoChat());
        MessaggioChatResponse response = messaggioChatFacade.sendMessage(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/gruppo/{gruppoNome}")
    public ResponseEntity<List<MessaggioChatResponse>> getGroupChatHistory(@PathVariable String gruppoNome) 
    {
        log.debug("Controller: Richiesta cronologia chat per Gruppo Nome: {}", gruppoNome);
        List<MessaggioChatResponse> messaggiList = messaggioChatFacade.getGroupChatHistoryByNome(gruppoNome);
        return ResponseEntity.ok(messaggiList);
    }

    @GetMapping("/privata/{altroUtenteId}")
    public ResponseEntity<List<MessaggioChatResponse>> getPrivateChatHistory(@PathVariable Long altroUtenteId) 
    {
        log.debug("Controller: Richiesta cronologia chat privata con Utente ID: {}", altroUtenteId);
        List<MessaggioChatResponse> messaggiList = messaggioChatFacade.getPrivateChatHistory(altroUtenteId);
        return ResponseEntity.ok(messaggiList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) 
    {
        log.warn("Controller: Ricevuta richiesta di eliminazione per messaggio ID: {}", id);
        messaggioChatFacade.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}

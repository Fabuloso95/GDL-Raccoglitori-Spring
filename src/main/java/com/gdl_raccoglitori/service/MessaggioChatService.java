package com.gdl_raccoglitori.service;

import java.util.*;
import com.gdl_raccoglitori.dto.request.MessaggioChatRequest;
import com.gdl_raccoglitori.model.*;

public interface MessaggioChatService
{
    MessaggioChat sendNewMessage(MessaggioChatRequest request, Utente mittente);
    List<MessaggioChat> findGroupChatMessages(String gruppoID);
    List<MessaggioChat> findPrivateChatMessages(Utente utenteA, Utente utenteB);
    MessaggioChat findById(Long id);
    void deleteMessage(Long messaggioId, Utente utenteRichiedente);

}

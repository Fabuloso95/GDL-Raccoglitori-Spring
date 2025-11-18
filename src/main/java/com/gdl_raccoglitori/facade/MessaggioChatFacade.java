package com.gdl_raccoglitori.facade;

import java.util.*;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;

public interface MessaggioChatFacade 
{
    MessaggioChatResponse sendMessage(MessaggioChatRequest request);
    List<MessaggioChatResponse> getGroupChatHistoryByNome(String gruppoID);
    List<MessaggioChatResponse> getPrivateChatHistory(Long altroUtenteId);
    void deleteMessage(Long messaggioId);
}

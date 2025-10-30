package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.gdl_raccoglitori.model.*;

public interface MessaggioChatRepository extends JpaRepository<MessaggioChat, Long>
{
	Optional<MessaggioChat> findById(Long id);
	
	List<MessaggioChat> findByGruppoIDOrderByDataInvioAsc(UUID gruppoID);
    
    @Query("SELECT m FROM MessaggioChat m " +
            "WHERE (m.mittente = :utenteA AND m.destinatario = :utenteB) " +
            "OR (m.mittente = :utenteB AND m.destinatario = :utenteA) " +
            "ORDER BY m.dataInvio ASC")
     List<MessaggioChat> findChatPrivataTra(Utente utenteA, Utente utenteB);
}

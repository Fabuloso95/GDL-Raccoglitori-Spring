package com.gdl_raccoglitori.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.gdl_raccoglitori.model.*;

public interface MessaggioChatRepository extends JpaRepository<MessaggioChat, Long>
{
	Optional<MessaggioChat> findById(Long id);
	List<MessaggioChat> findByGruppoIDOrderByDataInvioAsc(String gruppoID);
}

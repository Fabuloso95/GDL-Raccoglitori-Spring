package com.gdl_raccoglitori.service;

import java.time.YearMonth;
import java.util.Optional;
import com.gdl_raccoglitori.model.*;

public interface VotoUtenteService 
{
    VotoUtente findById(Long id);
    Optional<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, YearMonth meseVotazione);
}

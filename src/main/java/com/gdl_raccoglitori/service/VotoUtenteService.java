package com.gdl_raccoglitori.service;

import java.util.List;
import com.gdl_raccoglitori.model.*;

public interface VotoUtenteService 
{
    VotoUtente findById(Long id);
    List<VotoUtente> findByUtenteAndMeseVotazione(Utente utente, String meseVotazione);
}

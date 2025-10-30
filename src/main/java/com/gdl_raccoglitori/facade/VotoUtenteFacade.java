package com.gdl_raccoglitori.facade;

import com.gdl_raccoglitori.dto.response.VotoUtenteResponse;

public interface VotoUtenteFacade 
{
    VotoUtenteResponse findById(Long id);
    VotoUtenteResponse checkExistingVote(String meseVotazione);
}

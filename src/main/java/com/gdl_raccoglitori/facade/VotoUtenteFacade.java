package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.response.VotoUtenteResponse;

public interface VotoUtenteFacade 
{
    VotoUtenteResponse findById(Long id);
    List<VotoUtenteResponse> checkExistingVote(String meseVotazione);
}

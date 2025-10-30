package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;

public interface PropostaVotoFacade 
{
    PropostaVotoResponse createProposta(PropostaVotoRequest request);
    VotoUtenteResponse voteForProposta(VotoUtenteRequest request);
    List<PropostaVotoResponse> getProposteByMese(String meseVotazione);
    PropostaVotoResponse getWinnerProposta(String meseVotazione);
    PropostaVotoResponse findById(Long id);

}

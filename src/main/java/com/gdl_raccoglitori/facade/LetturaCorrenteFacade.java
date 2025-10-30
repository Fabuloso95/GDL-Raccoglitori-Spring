package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;

public interface LetturaCorrenteFacade 
{
    LetturaCorrenteResponse startNewReading(LetturaCorrenteRequest request);
    LetturaCorrenteResponse updateReadingProgress(Long letturaId, LetturaCorrenteUpdateRequest request);
    LetturaCorrenteResponse completeReading(Long letturaId);
    void deleteReading(Long letturaId);
    LetturaCorrenteResponse findById(Long id);
    List<LetturaCorrenteResponse> findMyReadings();
    List<LetturaCorrenteProgressResponse> getBookProgressOverview(Long libroId);

}

package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.request.CuriositaRequest;
import com.gdl_raccoglitori.dto.response.CuriositaResponse;

public interface CuriositaFacade 
{
    CuriositaResponse createCuriosita(CuriositaRequest request);
    CuriositaResponse updateCuriosita(Long curiositaId, CuriositaRequest request);
    void deleteCuriosita(Long curiositaId);
    CuriositaResponse findById(Long id);
    List<CuriositaResponse> findByLibroId(Long libroId);
    List<CuriositaResponse> findByLibroAndPagina(Long libroId, Integer paginaRiferimento);
}
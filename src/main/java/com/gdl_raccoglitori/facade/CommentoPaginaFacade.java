package com.gdl_raccoglitori.facade;

import java.util.List;
import com.gdl_raccoglitori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglitori.dto.response.CommentoPaginaResponse;

public interface CommentoPaginaFacade 
{
	CommentoPaginaResponse createCommento(CommentoPaginaRequest request);
	CommentoPaginaResponse updateCommento(Long commentoId, String nuovoContenuto);
	void deleteCommento(Long commentoId);
	CommentoPaginaResponse findById(Long id);
	List<CommentoPaginaResponse> findByLetturaAndPagina(Long letturaCorrenteId, Integer paginaRiferimento);
	List<CommentoPaginaResponse> findByAutoreId(Long utenteId);
}

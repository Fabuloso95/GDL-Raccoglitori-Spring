package com.gdl_raccoglitori.service;

import java.util.List;
import com.gdl_raccoglitori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglitori.model.*;

public interface CommentoPaginaService 
{
	CommentoPagina createCommento(CommentoPaginaRequest request, Utente utenteAutore);
	CommentoPagina updateCommentoContenuto(Long commentoId, String contenuto, Utente utenteRichiedente);
	void deleteCommento(Long commentoId, Utente utenteRichiedente);
	CommentoPagina findById(Long id);
	List<CommentoPagina> findByLetturaAndPagina(Long letturaCorrenteId, Integer paginaRiferimento);
	List<CommentoPagina> findByAutoreId(Long utenteId);
}

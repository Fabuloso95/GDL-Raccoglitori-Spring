package com.gdl_raccoglitori.controller;

import com.gdl_raccoglitori.dto.request.CommentoPaginaRequest;
import com.gdl_raccoglitori.dto.response.CommentoPaginaResponse;
import com.gdl_raccoglitori.facade.CommentoPaginaFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/commenti") 
@RequiredArgsConstructor
@Slf4j
public class CommentoPaginaController 
{
    private final CommentoPaginaFacade commentoPaginaFacade;

    @PostMapping
    public ResponseEntity<CommentoPaginaResponse> createCommento(@Valid @RequestBody CommentoPaginaRequest request) 
    {
        log.info("Controller: Ricevuta richiesta di creazione commento per lettura ID {} su pagina {}",
                 request.getLetturaCorrenteId(), request.getPaginaRiferimento());
        CommentoPaginaResponse response = commentoPaginaFacade.createCommento(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentoPaginaResponse> getCommentoById(@PathVariable Long id) 
    {
        log.debug("Controller: Ricevuta richiesta per commento ID: {}", id);
        CommentoPaginaResponse commento = commentoPaginaFacade.findById(id);
        return ResponseEntity.ok(commento);
    }
    
    @GetMapping("/lettura/{letturaCorrenteId}/pagina/{paginaRiferimento}")
    public ResponseEntity<List<CommentoPaginaResponse>> getCommentiByLetturaAndPagina(
            @PathVariable Long letturaCorrenteId, 
            @PathVariable Integer paginaRiferimento) 
    {
        log.debug("Controller: Richiesta commenti per lettura ID {} e pagina {}", letturaCorrenteId, paginaRiferimento);
        List<CommentoPaginaResponse> commenti = commentoPaginaFacade.findByLetturaAndPagina(letturaCorrenteId, paginaRiferimento);
        return ResponseEntity.ok(commenti);
    }

    @GetMapping("/autore/{utenteId}")
    public ResponseEntity<List<CommentoPaginaResponse>> getCommentiByAutore(@PathVariable Long utenteId) 
    {
        log.debug("Controller: Richiesta commenti per autore ID: {}", utenteId);
        List<CommentoPaginaResponse> commenti = commentoPaginaFacade.findByAutoreId(utenteId);
        return ResponseEntity.ok(commenti);
    }

    @PatchMapping("/{id}/contenuto")
    public ResponseEntity<CommentoPaginaResponse> updateCommentoContenuto(
            @PathVariable Long id, 
            @RequestParam @NotBlank(message = "Il contenuto non può essere vuoto") String nuovoContenuto) 
    {
        log.info("Controller: Ricevuta richiesta di aggiornamento contenuto per commento ID: {}", id);
        CommentoPaginaResponse response = commentoPaginaFacade.updateCommento(id, nuovoContenuto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommento(@PathVariable Long id) 
    {
        log.warn("Controller: Ricevuta richiesta di eliminazione per commento ID: {}", id);
        commentoPaginaFacade.deleteCommento(id);
        return ResponseEntity.noContent().build();
    }
}

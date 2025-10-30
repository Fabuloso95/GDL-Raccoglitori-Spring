package com.gdl_raccoglitori.controller;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.UtenteResponse;
import com.gdl_raccoglitori.facade.UtenteFacade;
import com.gdl_raccoglitori.model.Ruolo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/utenti")
@Slf4j
@RequiredArgsConstructor
public class UtenteController 
{
	private final UtenteFacade utenteFacade;
	
	@PostMapping
	public ResponseEntity<UtenteResponse> createUtente(@RequestBody UtenteRequest request)
	{
		log.info("Controller: Ricevuta richiesta di creazione utente: {}", request.getUsername());
		UtenteResponse response = utenteFacade.create(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<UtenteResponse>> getAllUtenti()
	{
		log.info("Controller: Ricevuta richiesta per tutti gli utenti");
		List<UtenteResponse> response = utenteFacade.getAll();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UtenteResponse> getUtenteById(@PathVariable Long id)
	{
		log.info("Controller: Ricevuta richiesta per utente by id");
		UtenteResponse response = utenteFacade.getById(id);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UtenteResponse> updateUtente(@PathVariable Long id, @Valid @RequestBody UtenteUpdateRequest request)
	{
		log.info("Controller: Ricevuta richiesta per aggiornare utente by id"+ id);
		UtenteResponse response = utenteFacade.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) 
    {
        log.warn("Controller: Ricevuta richiesta di eliminazione per utente ID: {}", id);
        utenteFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
	
	@PatchMapping("/{id}/attiva")
    public ResponseEntity<UtenteResponse> attivaUtente(@PathVariable Long id) 
    {
        log.info("Controller: Attivazione utente ID: {}", id);
        UtenteResponse response = utenteFacade.attivaUtente(id);
        return ResponseEntity.ok(response);
    }
	
	@PatchMapping("/{id}/disattiva")
    public ResponseEntity<UtenteResponse> disattivaUtente(@PathVariable Long id) 
    {
        log.info("Controller: Disattivazione utente ID: {}", id);
        UtenteResponse response = utenteFacade.disattivaUtente(id);
        return ResponseEntity.ok(response);
    }
	
	@PatchMapping("/{id}/ruolo")
    public ResponseEntity<UtenteResponse> cambiaRuolo(@PathVariable Long id, @RequestParam Ruolo nuovoRuolo) 
    {
        log.info("Controller: Cambio ruolo utente ID {} al ruolo: {}", id, nuovoRuolo);
        UtenteResponse response = utenteFacade.cambiaRuolo(id, nuovoRuolo);
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/cerca")
    public ResponseEntity<List<UtenteResponse>> searchUtenti(@RequestParam String term) 
    {
        log.debug("Controller: Ricerca utenti per termine: {}", term);
        List<UtenteResponse> users = utenteFacade.search(term);
        return ResponseEntity.ok(users);
    }
	
	@GetMapping("/filtra/ruolo")
    public ResponseEntity<List<UtenteResponse>> findByRuolo(@RequestParam Ruolo ruolo) 
    {
        log.debug("Controller: Ricerca utenti per ruolo: {}", ruolo);
        List<UtenteResponse> users = utenteFacade.findByRuolo(ruolo);
        return ResponseEntity.ok(users);
    }
	
	@PostMapping("/password/dimenticata")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) 
    {
        log.info("Controller: Richiesta forgot password per: {}", email);
        utenteFacade.forgotPassword(email);
        return ResponseEntity.accepted().build();
    }
	
	@PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, 
                                              @RequestParam String nuovaPassword) 
    {
        log.info("Controller: Richiesta reset password con token.");
        utenteFacade.resetPassword(token, nuovaPassword);
        return ResponseEntity.ok().build();
    }
}

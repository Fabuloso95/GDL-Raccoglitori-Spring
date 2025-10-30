package com.gdl_raccoglitori.controller;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.gdl_raccoglitori.dto.request.FrasePreferitaRequest;
import com.gdl_raccoglitori.dto.response.FrasePreferitaResponse;
import com.gdl_raccoglitori.facade.FrasePreferitaFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/frasi-preferite")
@RequiredArgsConstructor
@Slf4j
public class FrasePreferitaController 
{

	private final FrasePreferitaFacade frasePreferitaFacade;
	
	@PostMapping
	public ResponseEntity<FrasePreferitaResponse> saveFrase(@Valid @RequestBody FrasePreferitaRequest request)
	{
		log.info("Controller: Ricevuta richiesta per salvare una frase preferita per il libro ID {}", request.getLibroId());
		FrasePreferitaResponse response = frasePreferitaFacade.saveFrase(request);
		return new ResponseEntity<FrasePreferitaResponse>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/me")
	public ResponseEntity<List<FrasePreferitaResponse>> getMyFrasiPreferite()
	{
		log.debug("Controller: Richiesta frasi preferite dell'utente autenticato.");
		List<FrasePreferitaResponse> frasi = frasePreferitaFacade.findMyFrasi();
		return ResponseEntity.ok(frasi);
	}
	
	@GetMapping("/libro/{libroId}")
    public ResponseEntity<List<FrasePreferitaResponse>> getFrasiByLibro(@PathVariable Long libroId) 
    {
        log.debug("Controller: Richiesta frasi preferite per libro ID: {}", libroId);
        List<FrasePreferitaResponse> frasiList = frasePreferitaFacade.findByLibroId(libroId);
        return ResponseEntity.ok(frasiList);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<FrasePreferitaResponse> getFrasePreferitaById(@PathVariable Long id) 
    {
        log.debug("Controller: Ricevuta richiesta per frase preferita ID: {}", id);
        FrasePreferitaResponse frase = frasePreferitaFacade.findById(id);
        return ResponseEntity.ok(frase);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrase(@PathVariable Long id) 
    {
        log.warn("Controller: Ricevuta richiesta di eliminazione per frase preferita ID: {}", id);
        frasePreferitaFacade.deleteFrase(id);
        return ResponseEntity.noContent().build();
    }
}

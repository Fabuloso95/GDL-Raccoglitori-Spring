package com.gdl_raccoglitori.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.gdl_raccoglitori.dto.request.FrasePreferitaRequest;
import com.gdl_raccoglitori.exceptionhandler.exception.*;
import com.gdl_raccoglitori.model.FrasePreferita;
import com.gdl_raccoglitori.model.Libro;
import com.gdl_raccoglitori.model.Utente;
import com.gdl_raccoglitori.repository.FrasePreferitaRepository;
import com.gdl_raccoglitori.repository.LibroRepository;
import com.gdl_raccoglitori.service.FrasePreferitaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrasePrefereritaServiceImpl implements FrasePreferitaService
{
	private final FrasePreferitaRepository frasePreferitaRepository;
	private final LibroRepository libroRepository;
	
	@Override
	public FrasePreferita saveFrasePreferita(FrasePreferitaRequest request, Utente utenteSalvatore)
	{
		Libro libro = libroRepository.findById(request.getLibroId()).orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + request.getLibroId() + " NON TROVATO."));
		FrasePreferita frase = new FrasePreferita();
		frase.setUtente(utenteSalvatore);
		frase.setLibro(libro);
		frase.setTestoFrase(request.getTestoFrase());
		frase.setPaginaRiferimento(request.getPaginaRiferimento());
		
		log.info("L'utente {} sta salvando una frase per il libro {}", 
                utenteSalvatore.getUsername(), libro.getTitolo());
		
		return frasePreferitaRepository.save(frase);
	}
	
	@Override
    public void deleteFrasePreferita(Long fraseId, Utente utenteRichiedente) 
    {
        FrasePreferita frase = findById(fraseId);

        if (!frase.getUtente().getId().equals(utenteRichiedente.getId())) 
        {
            log.warn("Tentativo non autorizzato di eliminare la frase preferita ID {} da parte dell'utente ID {}", fraseId, utenteRichiedente.getId());
            throw new OperazioneNonAutorizzataException("Non autorizzato ad eliminare questa frase preferita. Solo il proprietario può farlo.");
        }

        log.info("Eliminazione frase preferita ID {} da parte del proprietario ID {}", fraseId, utenteRichiedente.getId());
        frasePreferitaRepository.delete(frase);
    }
	
	@Override
    public FrasePreferita findById(Long id) 
    {
        return frasePreferitaRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Frase preferita con ID " + id + " non trovata."));
    }
	
	@Override
    public List<FrasePreferita> findByUtente(Utente utenteSalvatore) 
    {
        return frasePreferitaRepository.findByUtente(utenteSalvatore);
    }

    @Override
    public List<FrasePreferita> findByLibroId(Long libroId) 
    {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + libroId + " non trovato."));
        
        return frasePreferitaRepository.findByLibro(libro);
    }
}

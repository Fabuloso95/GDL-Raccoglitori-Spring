package com.gdl_raccoglitori.service.impl;

import com.gdl_raccoglitori.dto.request.PropostaVotoRequest;
import com.gdl_raccoglitori.exceptionhandler.exception.*;
import com.gdl_raccoglitori.mapper.PropostaVotoMapper;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.*;
import com.gdl_raccoglitori.service.PropostaVotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropostaVotoServiceImpl implements PropostaVotoService 
{
    private final PropostaVotoRepository propostaVotoRepository;
    private final LibroRepository libroRepository;
    private final VotoUtenteRepository votoUtenteRepository;
    private final PropostaVotoMapper propostaVotoMapper;
    private static final int MAX_VOTI_PER_MESE = 3;

    @Override
    public PropostaVoto createProposta(PropostaVotoRequest request) 
    {
        String meseVotazione = request.getMeseVotazione();
        
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + request.getLibroId() + " non trovato."));

        List<PropostaVoto> existingProposte = propostaVotoRepository.findByMeseVotazione(meseVotazione);
        boolean alreadyProposed = existingProposte.stream()
                .anyMatch(p -> p.getLibroProposto().getId().equals(request.getLibroId()));

        if (alreadyProposed)
        {
            throw new ConflittoDatiException("Il libro è già stato proposto per la votazione di " + meseVotazione);
        }

        PropostaVoto proposta = propostaVotoMapper.toEntity(request);
        proposta.setLibroProposto(libro);
        proposta.setDataCreazione(LocalDateTime.now());
        proposta.setNumVoti(0);
        proposta.setMeseVotazione(meseVotazione); 
        
        log.info("Creata nuova proposta per il libro ID {} per il mese {}", request.getLibroId(), meseVotazione);

        return propostaVotoRepository.save(proposta);
    }
    
    @Override
    public PropostaVoto updateProposta(Long propostaId, PropostaVotoRequest request, Utente utenteCreatore) 
    {
        PropostaVoto proposta = findById(propostaId);

        Libro nuovoLibro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + request.getLibroId() + " non trovato."));
        
        String nuovoMese = request.getMeseVotazione();
        
        if (!proposta.getMeseVotazione().equals(nuovoMese) || !proposta.getLibroProposto().getId().equals(request.getLibroId())) 
        {
            
            List<PropostaVoto> existingProposte = propostaVotoRepository.findByMeseVotazione(nuovoMese);
            boolean isDuplicate = existingProposte.stream()
                .filter(p -> !p.getId().equals(propostaId)) 
                .anyMatch(p -> p.getLibroProposto().getId().equals(request.getLibroId()));

            if (isDuplicate)
            {
                 throw new ConflittoDatiException("Il libro è già stato proposto per la votazione di " + nuovoMese);
            }
        }
        
        proposta.setLibroProposto(nuovoLibro);
        proposta.setMeseVotazione(nuovoMese);
        
        log.info("Proposta ID {} aggiornata per il libro ID {} per il mese {}", propostaId, request.getLibroId(), nuovoMese);
        return propostaVotoRepository.save(proposta);
    }
    
    @Override
    @Transactional
    public void deleteProposta(Long propostaId) 
    {
        PropostaVoto proposta = findById(propostaId);
        
        propostaVotoRepository.delete(proposta);
        log.warn("Proposta ID {} eliminata con successo.", propostaId);
    }
    
    @Override
    public PropostaVoto findWinnerProposta(String meseVotazione) 
    {
        log.debug("Ricerca proposta vincente per il mese: '{}'", meseVotazione);
        
        List<PropostaVoto> tutteProposte = propostaVotoRepository.findByMeseVotazione(meseVotazione);
        log.debug("Trovate {} proposte per il mese: {}", tutteProposte.size(), meseVotazione);
        
        for (PropostaVoto proposta : tutteProposte) 
        {
            log.debug("Proposta ID: {}, Libro: {}, Voti: {}, Mese: {}", 
                     proposta.getId(), 
                     proposta.getLibroProposto().getId(),
                     proposta.getNumVoti(),
                     proposta.getMeseVotazione());
        }
        
        PropostaVoto winner = propostaVotoRepository.findTopPropostaByMeseVotazione(meseVotazione);
        
        log.debug("Vincitore trovato: {}", winner != null ? "ID " + winner.getId() : "NULL");
        
        if (winner == null)
        {
             throw new RisorsaNonTrovataException("Nessuna proposta trovata per il mese " + meseVotazione);
        }
        
        return winner;
    }

    @Override
    public List<PropostaVoto> findByMeseVotazione(String meseVotazione) 
    {
        return propostaVotoRepository.findByMeseVotazione(meseVotazione);
    }
    
    @Override
    public PropostaVoto findById(Long id) 
    {
        return propostaVotoRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Proposta di Voto con ID " + id + " non trovata."));
    }

    @Override
    @Transactional
    public VotoUtente registeVoto(Long propostaVotoId, Utente utente, String meseVotazioneStr) 
    {
        PropostaVoto proposta = findById(propostaVotoId);
        String meseVotazioneProposta = proposta.getMeseVotazione(); 
        

        if (!meseVotazioneProposta.equals(meseVotazioneStr))
        {
            throw new ConflittoDatiException(
                "La proposta ID " + propostaVotoId + " è per il mese " + meseVotazioneProposta + 
                ", ma il voto è richiesto per " + meseVotazioneStr + ". Il voto non è coerente."
            );
        }
        
        if (votoUtenteRepository.findByUtenteAndPropostaVoto(utente, proposta).isPresent())
        {
            throw new ConflittoDatiException("L'utente ID " + utente.getId() + " ha già votato per questa specifica proposta (ID " + propostaVotoId + ").");
        }

        List<VotoUtente> votiEsistentiNelMese = votoUtenteRepository.findByUtenteAndMeseVotazione(utente, meseVotazioneStr);
        
        if (votiEsistentiNelMese.size() >= MAX_VOTI_PER_MESE) 
        {
            throw new LimiteSuperatoException(
                "Hai raggiunto il limite massimo di " + MAX_VOTI_PER_MESE + 
                " voti per la votazione di " + meseVotazioneStr + " e non puoi votare per nuove proposte."
            );
        }

        VotoUtente voto = new VotoUtente();
        voto.setUtente(utente);
        voto.setPropostaVoto(proposta);
        voto.setMeseVotazione(meseVotazioneStr); 
        VotoUtente savedVoto = votoUtenteRepository.save(voto);

        proposta.setNumVoti(proposta.getNumVoti() + 1);
        propostaVotoRepository.save(proposta);
        
        log.info("Voto registrato per l'utente ID {} sulla proposta ID {} per il mese {}. Voti totali nel mese per l'utente: {}", 
                 utente.getId(), propostaVotoId, meseVotazioneStr, votiEsistentiNelMese.size() + 1);

        return savedVoto;
    }
}

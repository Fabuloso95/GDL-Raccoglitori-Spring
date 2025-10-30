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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public PropostaVoto createProposta(PropostaVotoRequest request) 
    {
        YearMonth meseVotazione = propostaVotoMapper.stringToYearMonth(request.getMeseVotazione());
        
        libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RisorsaNonTrovataException("Libro con ID " + request.getLibroId() + " non trovato."));

        List<PropostaVoto> existingProposte = propostaVotoRepository.findByMeseVotazione(meseVotazione);
        boolean alreadyProposed = existingProposte.stream()
                .anyMatch(p -> p.getLibroProposto().getId().equals(request.getLibroId()));

        if (alreadyProposed)
        {
            throw new ConflittoDatiException("Il libro è già stato proposto per la votazione di " + meseVotazione.format(FORMATTER));
        }

        PropostaVoto proposta = propostaVotoMapper.toEntity(request);
        proposta.setDataCreazione(LocalDateTime.now());
        proposta.setNumVoti(0);
        
        log.info("Creata nuova proposta per il libro ID {} per il mese {}", request.getLibroId(), meseVotazione.format(FORMATTER));

        return propostaVotoRepository.save(proposta);
    }

    @Override
    public PropostaVoto findWinnerProposta(YearMonth meseVotazione) 
    {
        log.debug("Ricerca proposta vincente per il mese {}", meseVotazione.format(FORMATTER));
        
        PropostaVoto winner = propostaVotoRepository.findTopPropostaByMeseVotazione(meseVotazione);
        
        if (winner == null)
        {
             throw new RisorsaNonTrovataException("Nessuna proposta trovata per il mese " + meseVotazione.format(FORMATTER));
        }
        
        return winner;
    }

    @Override
    public List<PropostaVoto> findByMeseVotazione(YearMonth meseVotazione) 
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
        YearMonth meseVotazioneProposta = proposta.getMeseVotazione();
        
        YearMonth meseRichiesto;
        try 
        {
            meseRichiesto = YearMonth.parse(meseVotazioneStr, FORMATTER);
        } 
        catch (Exception e) 
        {
            throw new IllegalArgumentException("Formato mese/anno ('" + meseVotazioneStr + "') non valido. Usa YYYY-MM.");
        }

        if (!meseVotazioneProposta.equals(meseRichiesto))
        {
            throw new ConflittoDatiException(
                "La proposta ID " + propostaVotoId + " è per il mese " + meseVotazioneProposta.format(FORMATTER) + 
                ", ma il voto è richiesto per " + meseRichiesto.format(FORMATTER) + ". Il voto non è coerente."
            );
        }

        if (votoUtenteRepository.findByUtenteAndMeseVotazione(utente, meseRichiesto).isPresent()) 
        {
            throw new ConflittoDatiException("L'utente ID " + utente.getId() + " ha già espresso un voto per la votazione di " + meseVotazioneStr);
        }

        VotoUtente voto = new VotoUtente();
        voto.setUtente(utente);
        voto.setPropostaVoto(proposta);
        voto.setMeseVotazione(meseRichiesto); 
        VotoUtente savedVoto = votoUtenteRepository.save(voto);

        proposta.setNumVoti(proposta.getNumVoti() + 1);
        propostaVotoRepository.save(proposta);
        
        log.info("Voto registrato per l'utente ID {} sulla proposta ID {} per il mese {}", 
                 utente.getId(), propostaVotoId, meseVotazioneStr);

        return savedVoto;
    }
}

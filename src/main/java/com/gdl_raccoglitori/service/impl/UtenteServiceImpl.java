package com.gdl_raccoglitori.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gdl_raccoglitori.dto.request.UtenteRequest;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.UtenteRepository;
import com.gdl_raccoglitori.security.JwtService;
import com.gdl_raccoglitori.service.*;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService
{
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public Utente getCurrentAuthenticatedUser() 
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Utente)
        {
            return (Utente) principal;
        } 
        else if (principal instanceof UserDetails)
        {
            String username = ((UserDetails) principal).getUsername();
            return findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utente autenticato non trovato nel database: " + username));
        }
        
        throw new RuntimeException("Nessun utente autenticato trovato nel Security Context.");
    }
    
    @Override
    public List<Utente> findAll()
    {
        return utenteRepository.findAll();
    }

    @Override
    public Optional<Utente> findById(Long id)
    {
        return utenteRepository.findById(id);
    }

    @Override
    public Utente findByEmail(String email)
    {
        return utenteRepository.findByEmail(email).orElse(null); 
    }

    @Override
    public Optional<Utente> findByUsername(String username)
    {
        return utenteRepository.findByUsername(username);
    }

    @Override
    public List<Utente> findByNome(String nome)
    {
        return utenteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Utente> findByCognome(String cognome)
    {
        return utenteRepository.findByCognomeContainingIgnoreCase(cognome);
    }

    @Override
    public List<Utente> findByNomeAndCognome(String nome, String cognome)
    {
        return utenteRepository.findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(nome, cognome);
    }

    @Override
    public List<Utente> search(String term)
    {
        return utenteRepository.searchUtenti(term);
    }

    @Override
    public Utente save(Utente utente)
    {
        if (utente.getPassword() != null && !utente.getPassword().isEmpty() && !utente.getPassword().startsWith("$2a$"))
        {
            utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        }
        return utenteRepository.save(utente);
    }

    @Override
    public Utente updateUtente(Long id, UtenteRequest utenteDTO)
    {
        Utente utenteEsistente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));

        if (utenteDTO.getEmail() != null && !utenteDTO.getEmail().isEmpty()) 
        {
        	utenteEsistente.setEmail(utenteDTO.getEmail());
        }
        
        if (utenteDTO.getUsername() != null && !utenteDTO.getUsername().isEmpty()) 
        {
        	utenteEsistente.setUsername(utenteDTO.getUsername());
        }
        
        if (utenteDTO.getNome() != null && !utenteDTO.getNome().isEmpty()) 
        {
        	utenteEsistente.setNome(utenteDTO.getNome());
        }
        
        if (utenteDTO.getCognome() != null && !utenteDTO.getCognome().isEmpty()) 
        {
        	utenteEsistente.setCognome(utenteDTO.getCognome());
        }
        
        if (utenteDTO.getRuolo() != null) 
        {
        	utenteEsistente.setRuolo(utenteDTO.getRuolo());
        }
        
        if (utenteDTO.getPassword() != null && !utenteDTO.getPassword().isEmpty()) 
        {
            utenteEsistente.setPassword(passwordEncoder.encode(utenteDTO.getPassword()));
        }

        return utenteRepository.save(utenteEsistente);
    }

    @Override
    public Utente attivaUtente(Long id)
    {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));
        utente.setAttivo(true);
        return utenteRepository.save(utente);
    }

    @Override
    public Utente disattivaUtente(Long id)
    {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));
        utente.setAttivo(false);
        return utenteRepository.save(utente);
    }

    @Override
    public Utente cambiaRuolo(Long id, Ruolo nuovoRuolo)
    {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));
        utente.setRuolo(nuovoRuolo);
        return utenteRepository.save(utente);
    }

    @Override
    public void deleteById(Long id)
    {
        utenteRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return utenteRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username)
    {
        return utenteRepository.existsByUsername(username);
    }

    @Override
    public void forgotPassword(String email)
    {
        Utente utente = utenteRepository.findByEmail(email).orElse(null);
        if (utente != null)
        {
            String token = jwtService.generateResetPasswordToken(utente);
            utente.setResetPasswordToken(token);
            utente.setResetPasswordTokenExpiry(jwtService.extractExpirationAsLocalDateTime(token));
            utenteRepository.save(utente);
            // Assumo che emailService esista e funzioni correttamente
            // emailService.sendResetPasswordEmail(email, token); 
        }
        else
        {
            System.out.println("Tentativo di recupero password per email non esistente: " + email);
        }
    }

    @Override
    public void resetPassword(String token, String nuovaPassword)
    {
        Utente utente = utenteRepository.findByResetPasswordToken(token);

        if (utente == null || utente.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now()))
        {
            throw new RuntimeException("Token di reset non valido o scaduto");
        }

        if (!jwtService.isTokenValid(token, utente))
        {
            throw new RuntimeException("Token di reset non valido o scaduto");
        }

        utente.setPassword(passwordEncoder.encode(nuovaPassword));
        utente.setResetPasswordToken(null);
        utente.setResetPasswordTokenExpiry(null);
        utenteRepository.save(utente);
    }

    @Override
    public Utente updatePassword(Long id, String nuovaPassword)
    {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + id));
        String passwordCifrata = passwordEncoder.encode(nuovaPassword);
        utente.setPassword(passwordCifrata);
        return utenteRepository.save(utente);
    }
    
    @Override
    public Optional<Utente> findByEmailOptional(String email)
    {
        return utenteRepository.findByEmail(email);
    }
    
    @Override
    public List<Utente> findByRuolo(Ruolo ruolo) 
    {
        return utenteRepository.findByRuolo(ruolo);
    }
    
    @Override
    public Utente registerSocialUser(String email, String fullName)
    {
        String[] parts = fullName.split(" ", 2);
        String firstName = parts[0];
        String lastName = (parts.length > 1) ? parts[1] : "";

        if (lastName.isEmpty()) 
        {
            lastName = "SocialUser";
        }

        return registerNewSocialUser(email, firstName, lastName, "google", UUID.randomUUID().toString());
    }

    @Override
    public Utente registerNewSocialUser(String email, String nome, String cognome, String provider, String providerId)
    {
        String randomPassword = UUID.randomUUID().toString();
        
        Utente newUtente = new Utente();
        newUtente.setUsername(email);
        newUtente.setNome(nome);
        newUtente.setCognome(cognome);
        newUtente.setEmail(email);
        newUtente.setPassword(passwordEncoder.encode(randomPassword));
        newUtente.setRuolo(Ruolo.USER);
        newUtente.setProvider(provider);
        newUtente.setProviderId(providerId);
        newUtente.setAttivo(true);
        
        return utenteRepository.save(newUtente);
    }
}

package com.gdl_raccoglitori.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.UtenteRepository;
import com.gdl_raccoglitori.security.*;
import com.gdl_raccoglitori.service.AuthService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService
{
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // L'uso di @Lazy è necessario su AuthenticationManager per prevenire dipendenze circolari
    public AuthServiceImpl(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, JwtService jwtService, @Lazy AuthenticationManager authenticationManager)
    {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private RuoloDTO mapRuoloToDTO(Ruolo ruolo) 
    {
        Long id;
        switch (ruolo) 
        {
            case USER:
                id = 1L;
                break;
            case ADMIN:
                id = 2L;
                break;
            default:
                id = 99L; 
        }
        return new RuoloDTO(id, ruolo.name());
    }

    @Override
    @Transactional
    public UtenteResponse registraCliente(RegistrazioneRequest registrazioneDTO)
    {
        if (utenteRepository.existsByUsername(registrazioneDTO.getUsername()))
        {
            throw new RuntimeException("Username già in uso");
        }
        if (utenteRepository.existsByEmail(registrazioneDTO.getEmail())) 
        {
            throw new RuntimeException("Email già in uso");
        }

        Utente nuovoUtente = Utente.builder()
            .nome(registrazioneDTO.getNome())
            .cognome(registrazioneDTO.getCognome())
            .username(registrazioneDTO.getUsername())
            .email(registrazioneDTO.getEmail())
            .ruolo(Ruolo.USER)
            .attivo(true)
            .dataRegistrazione(LocalDate.now())
            .build();
            
        nuovoUtente.setPassword(passwordEncoder.encode(registrazioneDTO.getPassword()));
        
        Utente utenteRegistrato = utenteRepository.save(nuovoUtente);
        
        return UtenteResponse.builder()
            .id(utenteRegistrato.getId())
            .email(utenteRegistrato.getEmail())
    	    .username(utenteRegistrato.getUsername())
            .nome(utenteRegistrato.getNome())
            .cognome(utenteRegistrato.getCognome())
            .ruolo(mapRuoloToDTO(utenteRegistrato.getRuolo())) 
            .dataRegistrazione(utenteRegistrato.getDataRegistrazione())
            .attivo(utenteRegistrato.getAttivo())
            .build();
    }

    /**
     * Esegue l'accesso autenticando l'utente tramite username O email.
     * Genera e persiste il refresh token nel DB.
     */
    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginDTO)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        Utente utente = utenteRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail()) 
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato")); 

        String accessToken = jwtService.generateToken(utente);
        String refreshToken = jwtService.generateRefreshToken(utente);
        
        utente.setRefreshToken(refreshToken); 
        utenteRepository.save(utente);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setUsername(utente.getUsername());
        authResponse.setRuolo(utente.getRuolo().name());
        
        return authResponse;
    }

    /**
     * Genera un nuovo Access Token usando un Refresh Token valido.
     */
    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken)
    {
        final String username = jwtService.extractUsername(refreshToken);
        if (username == null)
        {
            throw new RuntimeException("Refresh token non valido o scaduto (impossibile estrarre username).");
        }

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato associato al token."));

        if (jwtService.isTokenExpired(refreshToken) || utente.getRefreshToken() == null || !refreshToken.equals(utente.getRefreshToken()))
        {
            if (utente.getRefreshToken() != null) 
            {
                utente.setRefreshToken(null);
                utenteRepository.save(utente);
            }
            throw new RuntimeException("Refresh token non valido o scaduto.");
        }

        String newAccessToken = jwtService.generateToken(utente);
        String newRefreshToken = jwtService.generateRefreshToken(utente); 
        
        utente.setRefreshToken(newRefreshToken);
        utenteRepository.save(utente);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setUsername(utente.getUsername());
        response.setRuolo(utente.getRuolo().name());

        return response;
    }
    
    /**
     * Invalida il Refresh Token disconnettendo l'utente (rimuovendolo dal DB).
     */
    @Override
    @Transactional
    public void logout(String refreshToken)
    {
        Utente utente = utenteRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token non valido."));

        utente.setRefreshToken(null);
        utenteRepository.save(utente);
    }

    /**
     * Implementazione di UserDetailsService: Carica i dettagli dell'utente per Spring Security.
     * Gestisce l'autenticazione tramite username O email.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException
    {
        Utente utente = utenteRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail) 
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username o email: " + usernameOrEmail));
        
        return new CustomUserDetails(utente);
    }

    /**
     * Recupera i dettagli completi dell'utente autenticato (UtenteResponse).
     */
    @Override
    @Transactional(readOnly = true)
    public UtenteResponse getCurrentUserDetails(Authentication authentication) 
    {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) 
        {
            throw new RuntimeException("Utente non autenticato o principal non valido.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Utente utente = userDetails.getUtente(); 
        
        return UtenteResponse.builder()
            .id(utente.getId())
            .email(utente.getEmail())
    	    .username(utente.getUsername())
            .nome(utente.getNome())
            .cognome(utente.getCognome())
            .ruolo(mapRuoloToDTO(utente.getRuolo())) 
            .dataRegistrazione(utente.getDataRegistrazione())
            .attivo(utente.getAttivo())
            .build();
    }
}

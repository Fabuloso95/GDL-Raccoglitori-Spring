package com.gdl_raccoglitori.controller;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.facade.AuthFacade;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController
{
    private final AuthFacade authFacade;

    public AuthController(AuthFacade authFacade) 
    {
        this.authFacade = authFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) 
    {
        AuthResponse response = authFacade.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteResponse> register(@Valid @RequestBody RegistrazioneRequest request) 
    {
    	UtenteResponse response = authFacade.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request)
    {
        AuthResponse response = authFacade.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO request)
    {
        authFacade.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<UtenteResponse> getCurrentUser(Authentication authentication)
    {
    	UtenteResponse userDetails = authFacade.getCurrentUserDetails(authentication);
        return ResponseEntity.ok(userDetails);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) 
    {
        Map<String, String> error = Map.of(
            "type", "INVALID_CREDENTIALS",
            "message", "Credenziali non valide. Per favore, riprova."
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) 
    {
        return new ResponseEntity<>(Map.of(
            "type", "INVALID_REQUEST",
            "message", ex.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGenericRuntimeException(RuntimeException ex)
    {
        return new ResponseEntity<>(Map.of(
            "type", "SERVER_ERROR",
            "message", "Si è verificato un errore del server."
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
package com.gdl_raccoglietori.facade;

import org.springframework.security.core.Authentication;
import com.gdl_raccoglietori.dto.request.*;
import com.gdl_raccoglietori.dto.response.*;


public interface AuthFacade 
{
    AuthResponse login(LoginRequest request);
    UtenteResponse register(RegistrazioneRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    UtenteResponse getCurrentUserDetails(Authentication authentication);
}
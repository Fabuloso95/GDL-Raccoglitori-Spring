package com.gdl_raccoglitori.facade;

import org.springframework.security.core.Authentication;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;


public interface AuthFacade 
{
    AuthResponse login(LoginRequest request);
    UtenteResponse register(RegistrazioneRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    UtenteResponse getCurrentUserDetails(Authentication authentication);
}
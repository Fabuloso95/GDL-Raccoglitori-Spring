package com.gdl_raccoglietori.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.gdl_raccoglietori.dto.request.*;
import com.gdl_raccoglietori.dto.response.*;

public interface AuthService extends UserDetailsService
{
    UtenteResponse registraCliente(RegistrazioneRequest registrazioneDTO);
    AuthResponse login(LoginRequest loginDTO);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    UtenteResponse getCurrentUserDetails(Authentication authentication);
}

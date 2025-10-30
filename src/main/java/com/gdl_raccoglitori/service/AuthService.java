package com.gdl_raccoglitori.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;

public interface AuthService extends UserDetailsService
{
    UtenteResponse registraCliente(RegistrazioneRequest registrazioneDTO);
    AuthResponse login(LoginRequest loginDTO);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    UtenteResponse getCurrentUserDetails(Authentication authentication);
}

package com.gdl_raccoglietori.service.impl;

import com.gdl_raccoglietori.repository.UtenteRepository;
import com.gdl_raccoglietori.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService 
{
    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException 
    {
        return utenteRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username o email: " + usernameOrEmail));
    }
}

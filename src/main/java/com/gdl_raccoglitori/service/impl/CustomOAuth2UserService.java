package com.gdl_raccoglitori.service.impl;

import java.util.Optional;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.UtenteRepository;
import com.gdl_raccoglitori.security.CustomUserDetails;
import com.gdl_raccoglitori.service.UtenteService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService
{
    private final UtenteRepository utenteRepository;
    private final UtenteService utenteService;

    public CustomOAuth2UserService(UtenteRepository utenteRepository, UtenteService utenteService)
    {
        this.utenteRepository = utenteRepository;
        this.utenteService = utenteService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException
    {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try
        {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        }
        catch (Exception ex)
        {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)
    {
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String username = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        Optional<Utente> userOptional = utenteRepository.findByUsername(username);
        Utente utente;
        
        // Se l'utente esiste già, aggiorna i suoi dati
        if (userOptional.isPresent())
        {
            utente = userOptional.get();
        } 
        // Altrimenti, crea un nuovo utente
        else 
        {
            utente = registerNewOAuth2User(provider, username, name, firstName, lastName);
        }

        return new CustomUserDetails(utente, oAuth2User.getAttributes());
    }

    private Utente registerNewOAuth2User(String provider, String username, String name, String firstName, String lastName)
    {
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setNome(firstName != null ? firstName : name);
        utente.setCognome(lastName != null ? lastName : "");
        utente.setRuolo(Ruolo.USER);
        utente.setPassword(null);

        return utenteRepository.save(utente);
    }

	public UtenteService getUtenteService() 
	{
		return utenteService;
	}
}
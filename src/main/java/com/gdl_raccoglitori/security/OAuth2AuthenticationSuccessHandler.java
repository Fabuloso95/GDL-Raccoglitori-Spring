package com.gdl_raccoglitori.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.gdl_raccoglitori.model.Utente;
import com.gdl_raccoglitori.repository.UtenteRepository;
import com.gdl_raccoglitori.service.UtenteService;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler 
{
   private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
   
   private final UtenteService utenteService;
   private final JwtService jwtService;
   private final UtenteRepository utenteRepository;

   @Value("${custom.oauth2.authorized-redirect-uri}")
   private String redirectUri;

   public OAuth2AuthenticationSuccessHandler(UtenteService utenteService,
                                             JwtService jwtService,
                                             UtenteRepository utenteRepository)
   {
       this.utenteService = utenteService;
       this.jwtService = jwtService;
       this.utenteRepository = utenteRepository;
   }

   @Override
   public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException 
   {
       DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
       String email = oidcUser.getEmail();
       String nome = oidcUser.getFullName();
       
       Utente utente = utenteRepository.findByEmail(email)
       	    .orElseGet(() -> utenteService.registerSocialUser(email, nome));

       String jwtToken = jwtService.generateToken(utente);

       String finalRedirectUrl = redirectUri + "?token=" + jwtToken;
       log.info("Redirecting user {} to: {}", utente.getUsername(), finalRedirectUrl);

       getRedirectStrategy().sendRedirect(request, response, finalRedirectUrl);
   }
}
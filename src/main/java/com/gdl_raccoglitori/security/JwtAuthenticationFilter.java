package com.gdl_raccoglitori.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import com.gdl_raccoglitori.model.Utente; 

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            log.debug("Nessun token Bearer trovato nell'header per la richiesta: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            log.warn("Errore nell'estrazione username dal token JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails))
                {
                    Object principal = userDetails; 

                    if (userDetails instanceof CustomUserDetails) {
                        CustomUserDetails customDetails = (CustomUserDetails) userDetails;
                        principal = customDetails.getUtente(); 
                        log.debug("Estrazione Utente (JPA Entity) dal CustomUserDetails per il Principal.");
                    } else {
                        log.warn("Il UserDetails caricato non è CustomUserDetails. Usando l'oggetto originale come Principal.");
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            principal, 
                            null,        
                            userDetails.getAuthorities() 
                    );
                    
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Autenticazione JWT riuscita per l'utente: {}", username);

                } else {
                    log.warn("Tentativo di autenticazione JWT fallito: Token non valido per l'utente {}", username);
                }
            } catch (UsernameNotFoundException e) {
                log.warn("Tentativo di autenticazione JWT fallito: Utente {} non trovato nel DB.", username);
            } catch (Exception e) {
                log.error("Errore generico durante l'autenticazione JWT: {}", e.getMessage(), e);
            }
        } else if (username != null) {
            log.debug("Security Context già popolato o username non estratto per la richiesta: {}", request.getRequestURI());
        }
        
        filterChain.doFilter(request, response);
    }
}

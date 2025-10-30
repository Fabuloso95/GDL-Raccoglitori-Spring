package com.gdl_raccoglitori.security;

import java.util.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import com.gdl_raccoglitori.service.impl.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
{
	@Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService)
    {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() 
    {
        return new JwtAuthenticationEntryPoint();
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler
    ) throws Exception
    {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // --- Rotte Pubbliche (PermitAll) ---
                // Autenticazione & OAuth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/login/oauth2/**").permitAll()
                
                // Gestione Password
                .requestMatchers("/api/utenti/password/dimenticata").permitAll() 
                .requestMatchers("/api/utenti/password/reset").permitAll()
                
                // Consultazione Libri (Catalogo)
                .requestMatchers(HttpMethod.GET, "/api/libri").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/libri/{id}").permitAll()
                
                // --- Rotte che Richiedono Autenticazione (Authenticated) ---
                // Le altre rotte dei Libri (POST, PUT, DELETE)
                .requestMatchers("/api/libri/**").authenticated() 
                
                // Utenti (CRUD, attivazione, ruolo, ecc.)
                .requestMatchers("/api/utenti/**").authenticated() 
                
                // Letture
                .requestMatchers("/api/letture/**").authenticated() 
                
                // Commenti
                .requestMatchers("/api/commenti/**").authenticated()
                
                // Curiosità
                .requestMatchers("/api/curiosita/**").authenticated()
                
                // Frasi Preferite
                .requestMatchers("/api/frasi-preferite/**").authenticated()
                
                // Chat
                .requestMatchers("/api/chat/**").authenticated()
                
                // Proposte e Voti
                // Include: /api/proposte/** e /api/voti/** (anche il nuovo VotoUtenteController)
                .requestMatchers("/api/proposte/**").authenticated()
                .requestMatchers("/api/voti/**").authenticated()
                
                // Qualsiasi altra richiesta che non è stata coperta richiede autenticazione
                .anyRequest().authenticated())
                
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorization"))
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler));

        return http.build();
    }
	
	@Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }
}
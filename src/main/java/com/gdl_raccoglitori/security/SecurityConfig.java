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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import com.gdl_raccoglitori.service.impl.CustomOAuth2UserService;
import com.gdl_raccoglitori.service.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
{
    private final CustomUserDetailsService userDetailsService;
    
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Rotte Pubbliche
                .requestMatchers("/auth/**", "/api/auth/**", "/oauth2/**", 
                               "/login/oauth2/**", "/api/utenti/password/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/libri", "/api/libri/{id}").permitAll()
                
                // Tutte le altre richiedono autenticazione
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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
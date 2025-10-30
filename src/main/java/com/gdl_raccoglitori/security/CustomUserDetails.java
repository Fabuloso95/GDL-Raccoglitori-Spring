package com.gdl_raccoglitori.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.gdl_raccoglitori.model.Utente;
import lombok.*;
import java.util.*;

@Data
public class CustomUserDetails implements UserDetails, OAuth2User
{
    private static final long serialVersionUID = 1L;
    private final Utente utente;
    private Map<String, Object> attributes;

    public CustomUserDetails(Utente utente)
    {
        this.utente = utente;
    }

    public CustomUserDetails(Utente utente, Map<String, Object> attributes)
    {
        this.utente = utente;
        this.attributes = attributes;
    }

    public Long getId() 
    {
        return utente.getId();
    }

    public Utente getUtente()
    {
        return utente;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo().name()));
    }

    @Override
    public String getPassword()
    {
        return utente.getPassword();
    }

    @Override
    public String getUsername()
    {
        return utente.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return utente.getAttivo();
    }

    // OAuth2User methods
    @Override
    public Map<String, Object> getAttributes()
    {
        return attributes;
    }

    @Override
    public String getName()
    {
        if (attributes != null && attributes.get("name") != null) 
        {
            return (String) attributes.get("name");
        }
        return utente.getUsername();
    }
}
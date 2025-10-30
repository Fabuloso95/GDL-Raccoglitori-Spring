package com.gdl_raccoglitori.facade.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.gdl_raccoglitori.dto.request.*;
import com.gdl_raccoglitori.dto.response.*;
import com.gdl_raccoglitori.facade.AuthFacade;
import com.gdl_raccoglitori.service.AuthService;
import lombok.*;

@Service
@Data
@AllArgsConstructor
public class AuthFacadeImpl implements AuthFacade
{
	private final AuthService authService;
	
	@Override
	public AuthResponse login(LoginRequest request) 
	{
		return authService.login(request);
	}

	@Override
	public UtenteResponse register(RegistrazioneRequest request)
	{
		return authService.registraCliente(request);
	}

	@Override
	public AuthResponse refreshToken(String refreshToken)
	{
		return authService.refreshToken(refreshToken);
	}

	@Override
	public void logout(String refreshToken)
	{
		authService.logout(refreshToken);
	}

	@Override
	public UtenteResponse getCurrentUserDetails(Authentication authentication)
	{
		return authService.getCurrentUserDetails(authentication);
	}
}

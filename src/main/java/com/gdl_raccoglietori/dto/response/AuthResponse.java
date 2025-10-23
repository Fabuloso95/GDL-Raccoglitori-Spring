package com.gdl_raccoglietori.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AuthResponse 
{
	private String accessToken;
    private String refreshToken;
    private String username;
    private String ruolo;
}

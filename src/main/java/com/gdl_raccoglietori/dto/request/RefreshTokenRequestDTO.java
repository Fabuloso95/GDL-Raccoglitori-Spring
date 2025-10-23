package com.gdl_raccoglietori.dto.request;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RefreshTokenRequestDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "Il refresh token è obbligatorio")
    private String refreshToken;
}
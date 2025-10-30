package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NuovaPasswordRequest 
{
    @NotBlank(message = "La nuova password è obbligatoria")
    private String nuovaPassword;
}
package com.gdl_raccoglitori.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CookieConsentRequest
{
    @NotNull
    @JsonProperty("necessary")
    private Boolean necessary;
    
    @JsonProperty("analytics")
    private Boolean analytics = false;
    
    @JsonProperty("marketing")
    private Boolean marketing = false;
    
    @JsonProperty("preferences")
    private Boolean preferences = false;
    
    @NotNull
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @NotNull
    @JsonProperty("version")
    private String version;
    
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
}
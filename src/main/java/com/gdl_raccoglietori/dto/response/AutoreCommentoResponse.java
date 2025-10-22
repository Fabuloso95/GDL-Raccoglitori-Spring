package com.gdl_raccoglietori.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutoreCommentoResponse 
{
    private Long id;
    private String username;
}
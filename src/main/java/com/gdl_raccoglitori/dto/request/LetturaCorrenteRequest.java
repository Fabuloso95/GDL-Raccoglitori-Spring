package com.gdl_raccoglitori.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LetturaCorrenteRequest 
{
	@NotNull(message = "L'ID del libro è obbligatorio per iniziare una lettura")
    @Positive(message = "L'ID del libro deve essere un valore positivo")
    private Long libroId;
    
    // Se l'utente vuole impostare la pagina iniziale a zero o a un valore specifico.
    // Lo rendiamo facoltativo; se non specificato, il controller imposterà 0.
    @Positive(message = "La pagina iniziale deve essere un valore positivo")
    private Integer paginaIniziale;
}

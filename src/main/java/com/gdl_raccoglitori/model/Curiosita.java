package com.gdl_raccoglitori.model;

import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curiosita 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libro_id", nullable = false)
	private Libro libro;
	
	@Column(nullable = false)
	@NotBlank(message = "Il titolo è obbligatorio")
	private String titolo;
	
	@Lob
	@NotBlank(message = "Il contenuto è obbligatorio")
	private String contenuto;
	
	@Column(name = "pagina_di_riferimento")
	private Integer paginaRiferimento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "utente_creatore_id", nullable = false)
	private Utente utenteCreatore;
	
	@Override
    public int hashCode() 
	{
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Curiosita other = (Curiosita) obj;
        return Objects.equals(id, other.id);
    }
    
    @Override
    public String toString()
    {
        return "Curiosita [id=" + id + ", titolo=" + titolo + "]";
    }
}

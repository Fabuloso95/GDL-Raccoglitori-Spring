package com.gdl_raccoglietori.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Utente 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 100)
	@NotBlank(message = "il nome è obbligatorio")
	private String nome;
	
	@Column(length = 100)
	@NotBlank(message = "il cognome è obbligatorio")
	private String cognome;
	
	@Column(length = 60)
	private String password;
}

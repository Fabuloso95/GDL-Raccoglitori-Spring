package com.gdl_raccoglietori.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Utente implements UserDetails
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "l'username è obbligatorio")
	@Column(unique = true, nullable = false)
	@Min(value = 8, message = "l'username deve avere almeno 8 caratteri")
	private String username;
	
	@Column(length = 100, nullable = false)
	@NotBlank(message = "il nome è obbligatorio")
	private String nome;
	
	@Column(length = 100, nullable = false)
	@NotBlank(message = "il cognome è obbligatorio")
	private String cognome;
	
	@Column(length = 60, nullable = false)
	private String password;
	
	@Email(message = "Formato email non valido")
	@NotBlank(message = "l'email è obbligatoria")
	@Column(nullable = false, unique = true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Ruolo ruolo;
	
	@Column(nullable = false)
    private LocalDate dataRegistrazione = LocalDate.now();
	
	@Column(length = 255)
    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;

    @Column(length = 500)
    private String refreshToken;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{
		return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name().toUpperCase()));
	}

	@Override
	public String getUsername()
	{
		return username;
	}
}

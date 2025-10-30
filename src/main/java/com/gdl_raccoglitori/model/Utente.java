package com.gdl_raccoglitori.model;

import java.time.*;
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
@Builder
public class Utente implements UserDetails
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "l'username è obbligatorio")
	@Column(unique = true, nullable = false)
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
	@PastOrPresent(message = "la data di registrazione non può essere futura")
	@Builder.Default
    private LocalDate dataRegistrazione = LocalDate.now();
	
	@Column(length = 255)
    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;

    @Column(length = 500)
    private String refreshToken;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean attivo = true;

    @Column(length = 20)
    private String provider;

    @Column(length = 100)
    private String providerId;
    
    @OneToMany(mappedBy = "mittente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessaggioChat> messaggiInviati;
    
    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessaggioChat> messaggiRicevuti;
    
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LetturaCorrente> lettureCorrenti;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VotoUtente> votiUtente;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentoPagina> commentiPagina;
    
    @OneToMany(mappedBy = "utenteCreatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Curiosita> curiositaCreate;
    
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FrasePreferita> frasiPreferite;

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
	
	@Override
    public boolean isEnabled() 
    {
        return this.attivo;
    }
}
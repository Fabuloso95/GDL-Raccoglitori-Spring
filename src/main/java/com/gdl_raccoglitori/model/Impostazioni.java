package com.gdl_raccoglitori.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "impostazioni")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Impostazioni 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false, unique = true)
    private Utente utente;

    @Column(name = "notifiche_email", nullable = false)
    @Builder.Default
    private Boolean notificheEmail = true;

    @Column(name = "notifiche_push", nullable = false)
    @Builder.Default
    private Boolean notifichePush = true;

    @Column(name = "lingua", nullable = false, length = 10)
    @Builder.Default
    private String lingua = "it";

    @Column(name = "tema", nullable = false, length = 20)
    @Builder.Default
    private String tema = "system";

    @Column(name = "email_riassunto_settimanale", nullable = false)
    @Builder.Default
    private Boolean emailRiassuntoSettimanale = false;

    @Column(name = "privacy_profilo_pubblico", nullable = false)
    @Builder.Default
    private Boolean privacyProfiloPubblico = true;

    @Column(name = "data_aggiornamento", nullable = false)
    @Builder.Default
    private LocalDateTime dataAggiornamento = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() 
    {
        this.dataAggiornamento = LocalDateTime.now();
    }
}
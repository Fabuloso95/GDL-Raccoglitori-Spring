package com.gdl_raccoglitori.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eventi")
public class Evento 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titolo;
    
    private String descrizione;
    
    @Column(nullable = false)
    private LocalDateTime dataInizio;
    
    @Column(nullable = false)
    private LocalDateTime dataFine;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creato_da_id")
    private Utente creatoDa;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() 
    {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() 
    {
        updatedAt = LocalDateTime.now();
    }
}
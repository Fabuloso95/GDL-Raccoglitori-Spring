package com.gdl_raccoglitori.config;

import java.time.LocalDate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gdl_raccoglitori.model.*;
import com.gdl_raccoglitori.repository.UtenteRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AdminUserInitializer 
{
	private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    ApplicationRunner initializeAdminUser() 
    {
        return args -> {
            String emailAdmin = "fabioleonardi48@gmail.com";

            if (utenteRepository.findByEmail(emailAdmin).isEmpty())
            {
                Utente adminUser = new Utente();
                adminUser.setUsername("admin123");
                adminUser.setPassword(passwordEncoder.encode("PasswordSicura123!"));
                adminUser.setEmail(emailAdmin);
                adminUser.setNome("Fabio");
                adminUser.setCognome("Leonardi");
                adminUser.setRuolo(Ruolo.ADMIN);
                adminUser.setDataRegistrazione(LocalDate.now());
                adminUser.setAttivo(true);

                utenteRepository.save(adminUser);

                System.out.println("✅ Utente Admin di default creato con successo!");
            } 
            else 
            {
                System.out.println("ℹ️ Utente Admin già esistente, nessuna creazione effettuata.");
            }
        };
    }
}

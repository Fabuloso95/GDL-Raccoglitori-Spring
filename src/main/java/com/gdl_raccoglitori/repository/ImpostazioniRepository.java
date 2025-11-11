package com.gdl_raccoglitori.repository;

import com.gdl_raccoglitori.model.Impostazioni;
import com.gdl_raccoglitori.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ImpostazioniRepository extends JpaRepository<Impostazioni, Long>
{
    
    Optional<Impostazioni> findByUtente(Utente utente);
    
    Optional<Impostazioni> findByUtenteId(Long utenteId);
    
    @Query("SELECT COUNT(i) > 0 FROM Impostazioni i WHERE i.utente.id = :utenteId")
    boolean existsByUtenteId(@Param("utenteId") Long utenteId);
    
    void deleteByUtenteId(Long utenteId);
}
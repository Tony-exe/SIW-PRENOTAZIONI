package it.uniroma3.siw.siw_prenotazioni.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.siw_prenotazioni.model.Prenotazione;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
import it.uniroma3.siw.siw_prenotazioni.model.User;

public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long>{
    List<Prenotazione> findByServizioAndData(Servizio servizio, LocalDate data);

    @Query("SELECT p FROM Prenotazione p JOIN FETCH p.servizio where p.user = :user order by p.data, p.ora")
    List<Prenotazione> findByUserOrderByDataOra(@Param("user") User user);

    @Query("SELECT p FROM Prenotazione p JOIN FETCH p.servizio JOIN FETCH p.user order by p.data, p.ora")
    List<Prenotazione> findAllByUser();

    @Modifying
    @Query("DELETE FROM Prenotazione p WHERE p.servizio = :servizio AND p.data < :oggi")
    int deletePrenotazionePassate(@Param ("servizio") Servizio servizio, @Param("oggi") LocalDate oggi);

    boolean existsByServizioAndDataGreaterThanEqual(Servizio servizio, LocalDate data);
}

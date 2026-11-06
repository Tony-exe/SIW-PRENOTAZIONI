package it.uniroma3.siw.siw_prenotazioni.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_prenotazioni.model.Servizio;

public interface ServizioRepository extends CrudRepository<Servizio, Long>{
    List<Servizio> findAllByOrderByNomeAsc();

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    @Query("SELECT s FROM Servizio s JOIN FETCH s.insegnante ORDER BY s.nome")
    List<Servizio> findAllConInsegnante();
}

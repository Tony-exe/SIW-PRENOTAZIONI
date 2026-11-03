package it.uniroma3.siw.siw_prenotazioni.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_prenotazioni.model.Servizio;

public interface ServizioRepository extends CrudRepository<Servizio, Long>{
    List<Servizio> findAllByOrderByNomeAsc();

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}

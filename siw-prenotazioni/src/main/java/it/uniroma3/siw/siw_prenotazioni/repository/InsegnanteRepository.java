package it.uniroma3.siw.siw_prenotazioni.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_prenotazioni.model.Insegnante;

public interface InsegnanteRepository extends CrudRepository<Insegnante, Long>{
    List<Insegnante> findAllByOrderByCognomeAscNomeAsc();
}

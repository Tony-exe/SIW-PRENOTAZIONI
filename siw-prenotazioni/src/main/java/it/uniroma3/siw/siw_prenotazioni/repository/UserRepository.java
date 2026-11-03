package it.uniroma3.siw.siw_prenotazioni.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_prenotazioni.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

    Boolean existsByEmail(String email);
}

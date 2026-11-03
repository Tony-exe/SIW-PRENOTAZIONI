package it.uniroma3.siw.siw_prenotazioni.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.siw_prenotazioni.model.Credentials;
import java.util.Optional;


public interface CredentialsRepository extends CrudRepository<Credentials, Long>{
    Optional<Credentials> findByUsername(@Param("username") String username); 

    Boolean existsByUsername(String username);
}

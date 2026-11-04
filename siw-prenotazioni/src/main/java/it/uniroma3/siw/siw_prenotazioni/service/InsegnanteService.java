package it.uniroma3.siw.siw_prenotazioni.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_prenotazioni.exception.InsegnanteNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.model.Insegnante;
import it.uniroma3.siw.siw_prenotazioni.repository.InsegnanteRepository;

@Service
public class InsegnanteService {

    private final InsegnanteRepository insegnanteRepository;

    public InsegnanteService(InsegnanteRepository insegnanteRepository){
        this.insegnanteRepository = insegnanteRepository;
    }

    @Transactional (readOnly = true)
    public Insegnante getInsegnante(Long id){
        return insegnanteRepository.findById(id).orElseThrow(() -> new InsegnanteNotFoundException(id));
    }

    @Transactional (readOnly = true)
    public List<Insegnante> getTuttiInsegnanti(){
        return insegnanteRepository.findAllByOrderByCognomeAscNomeAsc();
    }
}

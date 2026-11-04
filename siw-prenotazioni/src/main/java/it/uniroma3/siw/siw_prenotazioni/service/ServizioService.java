package it.uniroma3.siw.siw_prenotazioni.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_prenotazioni.exception.ServizioConPrenotazioniFutureException;
import it.uniroma3.siw.siw_prenotazioni.exception.ServizioExistsException;
import it.uniroma3.siw.siw_prenotazioni.exception.ServizioNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
import it.uniroma3.siw.siw_prenotazioni.repository.PrenotazioneRepository;
import it.uniroma3.siw.siw_prenotazioni.repository.ServizioRepository;

@Service
public class ServizioService {

    private static final Logger logger = LoggerFactory.getLogger(PrenotazioneService.class);
    private final ServizioRepository servizioRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public ServizioService(ServizioRepository servizioRepository, PrenotazioneRepository prenotazioneRepository){
        this.servizioRepository = servizioRepository;
        this.prenotazioneRepository = prenotazioneRepository; 
    }

    @Transactional (readOnly = true)
    public Servizio getServizio(Long id){
        return servizioRepository.findById(id).orElseThrow(() -> new ServizioNotFoundException(id));
    }

    @Transactional (readOnly = true)
    public List<Servizio> getTuttiServizi(){
        return servizioRepository.findAllByOrderByNomeAsc();
    }

    @Transactional
    public Servizio crea(Servizio s){
        if(servizioRepository.existsByNome(s.getNome())){
            throw new ServizioExistsException(s.getNome());
        }
        Servizio servizio = servizioRepository.save(s);
        
        logger.info("Servizio creato: id={}", servizio.getId());
        return servizio;
    }

    @Transactional
    public Servizio aggiorna(Long id, Servizio dati){
        Servizio s = getServizio(id);
        if(servizioRepository.existsByNomeAndIdNot(dati.getNome(), id)){
            throw new ServizioExistsException(dati.getNome());
        }
        s.setNome(dati.getNome());
        s.setDescrizione(dati.getDescrizione());
        s.setPrezzo(dati.getPrezzo());
        s.setDurata(dati.getDurata());

        return servizioRepository.save(s);
    }

    @Transactional 
    public void elimina(Long id){
        Servizio servizio = getServizio(id);
        LocalDate oggi = LocalDate.now();
        if(prenotazioneRepository.existsByServizioAndDataGreaterThanEqual(servizio, oggi)){
            logger.warn("Eliminazione bloccata, servizio con prenotazioni future: id servizio{}", id);
            throw new ServizioConPrenotazioniFutureException(servizio.getNome());
        }
        int eliminate = prenotazioneRepository.deletePrenotazionePassate(servizio, oggi);
        servizioRepository.delete(servizio);

        logger.info("Servizio eliminato: id={}, prenotazioni eliminate={}", id, eliminate);
    }
}

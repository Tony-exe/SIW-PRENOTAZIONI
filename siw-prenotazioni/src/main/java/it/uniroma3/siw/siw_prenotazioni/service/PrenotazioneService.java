package it.uniroma3.siw.siw_prenotazioni.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_prenotazioni.exception.AccessoNegatoException;
import it.uniroma3.siw.siw_prenotazioni.exception.FuoriOrarioException;
import it.uniroma3.siw.siw_prenotazioni.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.ServizioNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.SlotNonDisponibileException;
import it.uniroma3.siw.siw_prenotazioni.model.Prenotazione;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
import it.uniroma3.siw.siw_prenotazioni.model.User;
import it.uniroma3.siw.siw_prenotazioni.repository.PrenotazioneRepository;
import it.uniroma3.siw.siw_prenotazioni.repository.ServizioRepository;

@Service
public class PrenotazioneService {

    private static final LocalTime ORA_APERTURA = LocalTime.of(9, 0);
    private static final LocalTime ORA_CHIUSURA = LocalTime.of(19, 0);
    private static final DayOfWeek GIORNO_CHIUSURA = DayOfWeek.SUNDAY;

    private static final Logger logger = LoggerFactory.getLogger(PrenotazioneService.class);
    private final PrenotazioneRepository prenotazioneRepository;
    private final ServizioRepository servizioRepository;

    public PrenotazioneService (PrenotazioneRepository prenotazioneRepository, ServizioRepository servizioRepository){
        this.prenotazioneRepository = prenotazioneRepository;
        this.servizioRepository = servizioRepository;
    }

    private void controlloOra(LocalTime ora, LocalDate data, int durata){
        LocalDateTime inizio = LocalDateTime.of(data,ora);
        if (data.getDayOfWeek() == GIORNO_CHIUSURA || ora.isAfter(ORA_CHIUSURA) || ora.plusMinutes(durata).isAfter(ORA_CHIUSURA) || ora.isBefore(ORA_APERTURA)){
            throw new FuoriOrarioException("La prenotazione è fuori dall'orario di lavoro.");
        }else if (inizio.isBefore(LocalDateTime.now())){
            throw new FuoriOrarioException("Non è possibile prenotare per una data passata.");
        }
        
    }

    private void controllaSovrapposizioni(Servizio servizio, LocalDate data, LocalTime ora, Long idDaEscludere){
        Integer durata = servizio.getDurata();
        
        for(Prenotazione prenotazione : prenotazioneRepository.findByServizioAndData(servizio, data)){
            if(!prenotazione.getId().equals(idDaEscludere)){
                LocalTime p_ora = prenotazione.getOra();
                if(ora.plusMinutes(durata).isAfter(p_ora) && p_ora.plusMinutes(durata).isAfter(ora)){
                    throw new SlotNonDisponibileException(ora);
                }
            }
        }
    }

    private void controllaProprietario(Prenotazione p, User user){
        if(!p.getUser().equals(user)){
            throw new AccessoNegatoException();
        }
    }

    @Transactional (isolation = Isolation.SERIALIZABLE)
    public Prenotazione prenota(Prenotazione p, Long servizioId, User user){
        Servizio servizio = servizioRepository.findById(servizioId).orElseThrow(() -> new ServizioNotFoundException(servizioId));
        p.setId(null);
        controlloOra(p.getOra(), p.getData(), servizio.getDurata());
        controllaSovrapposizioni(servizio, p.getData(), p.getOra(), p.getId());
        p.setServizio(servizio);
        p.setUser(user);
        Prenotazione prenotazione = prenotazioneRepository.save(p);

        logger.info("Prenotazione creata: id={}, utente={}", prenotazione.getId(), prenotazione.getUser().getId());
        return prenotazione;
    }

    @Transactional (readOnly = true)
    public Prenotazione getPrenotazione(Long id){
        return prenotazioneRepository.findById(id).orElseThrow(() -> new PrenotazioneNotFoundException(id));
    }

    @Transactional (readOnly = true)
    public List<Prenotazione> getMiePrenotazioni(User user){
        return prenotazioneRepository.findByUserOrderByDataOra(user);
    }

    @Transactional (readOnly = true)
    public List<Prenotazione> getTutte(){
        return prenotazioneRepository.findAllByUser();
    }

    @Transactional (isolation = Isolation.SERIALIZABLE)
    public void modifica(Long id, LocalDate data, LocalTime ora, User user){
        Prenotazione prenotazione = getPrenotazione(id);
        Servizio servizio = prenotazione.getServizio();
        
        controllaProprietario(prenotazione, user);
        controlloOra(ora, data, servizio.getDurata());
        controllaSovrapposizioni(servizio, data, ora, id);  
        
        prenotazione.setData(data);
        prenotazione.setOra(ora);
        prenotazioneRepository.save(prenotazione);
        logger.info("Prenotazione modificata: id={}", id);
    }

    @Transactional
    public void annulla(Long id, User user){
        Prenotazione prenotazione = getPrenotazione(id);
        controllaProprietario(prenotazione, user);
        prenotazioneRepository.delete(prenotazione);
        logger.info("Prenotazione annullata: id={}", id);
    }
    
}

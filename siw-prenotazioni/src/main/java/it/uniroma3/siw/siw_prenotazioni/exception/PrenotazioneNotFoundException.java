package it.uniroma3.siw.siw_prenotazioni.exception;

public class PrenotazioneNotFoundException extends RuntimeException{

    public PrenotazioneNotFoundException(Long id) {
        super("La prenotazione con id " + id + " non esiste.");
    }
}

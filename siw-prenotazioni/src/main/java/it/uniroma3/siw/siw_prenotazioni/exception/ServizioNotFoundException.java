package it.uniroma3.siw.siw_prenotazioni.exception;

public class ServizioNotFoundException extends RuntimeException{
    public ServizioNotFoundException (Long id){
        super("Il servizio con id " + id + " non esiste.");
    }
}

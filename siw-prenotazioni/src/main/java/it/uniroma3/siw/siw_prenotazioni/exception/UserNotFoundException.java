package it.uniroma3.siw.siw_prenotazioni.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id){
        super("L'utente con ID " + id + " non esiste.");
    }
}

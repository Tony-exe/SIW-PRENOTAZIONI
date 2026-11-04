package it.uniroma3.siw.siw_prenotazioni.exception;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException (String email){
        super("L'email " + email + " è già in uso.");
    }
}

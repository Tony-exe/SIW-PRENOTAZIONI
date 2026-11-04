package it.uniroma3.siw.siw_prenotazioni.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException (String username){
        super("Lo username " + username + " esiste già.");
    }
}

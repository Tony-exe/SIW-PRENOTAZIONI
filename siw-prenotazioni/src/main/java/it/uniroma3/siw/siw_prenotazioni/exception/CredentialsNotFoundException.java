package it.uniroma3.siw.siw_prenotazioni.exception;

public class CredentialsNotFoundException extends RuntimeException {
    public CredentialsNotFoundException (String username){
        super("Lo username " + username + " non è registrato.");
    }
}

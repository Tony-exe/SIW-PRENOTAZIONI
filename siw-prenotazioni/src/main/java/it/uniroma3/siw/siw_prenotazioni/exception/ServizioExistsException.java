package it.uniroma3.siw.siw_prenotazioni.exception;

public class ServizioExistsException extends RuntimeException{
    public ServizioExistsException (String nome){
        super("Il servizio " + nome + " già esiste.");
    }
}

package it.uniroma3.siw.siw_prenotazioni.exception;

public class AccessoNegatoException extends RuntimeException{
    public AccessoNegatoException(){
        super("La prenotazione non ti appartiene");
    }
}

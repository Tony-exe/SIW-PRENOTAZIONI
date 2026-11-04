package it.uniroma3.siw.siw_prenotazioni.exception;

public class FuoriOrarioException extends RuntimeException {
    public FuoriOrarioException (String msg){
        super(msg);
    }
}

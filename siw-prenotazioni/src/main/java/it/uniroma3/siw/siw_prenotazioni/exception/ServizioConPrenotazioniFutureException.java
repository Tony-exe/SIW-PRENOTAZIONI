package it.uniroma3.siw.siw_prenotazioni.exception;

public class ServizioConPrenotazioniFutureException extends RuntimeException{
    public ServizioConPrenotazioniFutureException (String nome){
        super("Il servizio " + nome + " ha delle prenotazioni future.");
    }
}

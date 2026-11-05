package it.uniroma3.siw.siw_prenotazioni.exception;

public class ServizioConPrenotazioniFutureException extends RuntimeException{
    public ServizioConPrenotazioniFutureException (String nome){
        super("Le lezioni per la materia " + nome + " ha delle prenotazioni future.");
    }
}

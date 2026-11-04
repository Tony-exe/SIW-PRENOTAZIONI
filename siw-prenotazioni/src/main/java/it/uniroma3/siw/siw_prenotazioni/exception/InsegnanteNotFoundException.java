package it.uniroma3.siw.siw_prenotazioni.exception;

public class InsegnanteNotFoundException extends RuntimeException{
    public InsegnanteNotFoundException (Long id){
        super("L'insegnante con id " + id + " non esiste.");
    }
}

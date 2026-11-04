package it.uniroma3.siw.siw_prenotazioni.exception;

import java.time.LocalTime;

/**
 * SlotNonDisponibileException
 */

public class SlotNonDisponibileException extends RuntimeException{
    public SlotNonDisponibileException(LocalTime ora){
        super("La prenotazione alle ore " + ora + " non è disponibile.");
    }
}

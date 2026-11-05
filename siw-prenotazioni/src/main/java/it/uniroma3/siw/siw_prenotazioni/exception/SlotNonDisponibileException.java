package it.uniroma3.siw.siw_prenotazioni.exception;

import java.time.LocalTime;

/**
 * SlotNonDisponibileException
 */

public class SlotNonDisponibileException extends RuntimeException{
    public SlotNonDisponibileException(LocalTime ora){
        super("L'insegnante è già impegnato in una lezione che si sovrappone alle ore " + ora + ": scegli un altro orario.");
    }
}

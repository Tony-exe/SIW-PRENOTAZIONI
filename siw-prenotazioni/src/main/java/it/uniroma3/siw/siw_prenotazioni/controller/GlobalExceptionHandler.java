package it.uniroma3.siw.siw_prenotazioni.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.uniroma3.siw.siw_prenotazioni.exception.InsegnanteNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.PrenotazioneNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.ServizioNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({ServizioNotFoundException.class, PrenotazioneNotFoundException.class, InsegnanteNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String globalHandler(RuntimeException e, Model model){
        model.addAttribute("messaggio", e.getMessage());
        return "error/404";
    }
}

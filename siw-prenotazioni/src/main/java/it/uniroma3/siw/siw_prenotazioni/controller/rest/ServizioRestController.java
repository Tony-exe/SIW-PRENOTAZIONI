package it.uniroma3.siw.siw_prenotazioni.controller.rest;

import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.siw_prenotazioni.exception.ServizioNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
import it.uniroma3.siw.siw_prenotazioni.service.ServizioService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping ("/rest/servizi")
public class ServizioRestController {
    private final ServizioService servizioService;

    public ServizioRestController(ServizioService servizioService) {
        this.servizioService = servizioService;
    }
    
    @GetMapping
    public List<Servizio> servizi() {
        return servizioService.getTuttiServizi();
    }

    @GetMapping("/{id}")
    public Servizio servizio(@PathVariable("id") Long id) {
        return servizioService.getServizio(id);
    }

    @ExceptionHandler(ServizioNotFoundException.class)
    public ResponseEntity<Void> handler(){
        return ResponseEntity.notFound().build();
    }
    
}

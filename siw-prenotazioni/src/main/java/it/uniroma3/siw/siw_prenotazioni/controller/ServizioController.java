package it.uniroma3.siw.siw_prenotazioni.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import it.uniroma3.siw.siw_prenotazioni.service.ServizioService;


@Controller
public class ServizioController {

    private final ServizioService servizioService;
    
    public ServizioController(ServizioService servizioService){
        this.servizioService = servizioService;
    }

    @GetMapping("/servizi")
    public String getMethodName(Model model) {
        model.addAttribute("servizi", servizioService.getTuttiServizi());
        return "servizi/list";
    }

    @GetMapping("/servizi/{id}")
    public String getMethodName(@PathVariable("id") Long id, Model model) {
        model.addAttribute("servizio", servizioService.getServizio(id));
        return "servizi/show";
    }
    
    
}

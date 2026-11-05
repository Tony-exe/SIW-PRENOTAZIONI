package it.uniroma3.siw.siw_prenotazioni.controller.admin;

import it.uniroma3.siw.siw_prenotazioni.service.ServizioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller 
public class AdminServizioController {
    
    private final ServizioService servizioService;

    AdminServizioController(ServizioService servizioService) {
        this.servizioService = servizioService;
    }

    @GetMapping("/admin/servizi")
    public String getMethodName(Model model) {
        model.addAttribute("servizi", servizioService.getTuttiServizi());
        return "admin/servizi/list";
    }
    
}

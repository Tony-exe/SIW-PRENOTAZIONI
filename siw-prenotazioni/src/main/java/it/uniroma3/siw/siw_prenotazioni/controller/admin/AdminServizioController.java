package it.uniroma3.siw.siw_prenotazioni.controller.admin;

import it.uniroma3.siw.siw_prenotazioni.exception.ServizioExistsException;
import it.uniroma3.siw.siw_prenotazioni.model.Servizio;
import it.uniroma3.siw.siw_prenotazioni.service.InsegnanteService;
import it.uniroma3.siw.siw_prenotazioni.service.ServizioService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminServizioController {

    private final ServizioService servizioService;
    private final InsegnanteService insegnanteService;

    public AdminServizioController(ServizioService servizioService, InsegnanteService insegnanteService) {
        this.servizioService = servizioService;
        this.insegnanteService = insegnanteService;
    }

    private String mostraForm(Model model) {
        model.addAttribute("insegnanti", insegnanteService.getTuttiInsegnanti());
        return "admin/servizi/form";
    }

    @GetMapping("/admin/servizi")
    public String elenco(Model model) {
        model.addAttribute("servizi", servizioService.getTuttiServizi());
        return "admin/servizi/list";
    }

    @GetMapping("/admin/servizi/new")
    public String createForm(Model model) {
        model.addAttribute("servizio", new Servizio());
        return mostraForm(model);
    }

    @PostMapping("/admin/servizi/new")
    public String create(@Valid @ModelAttribute("servizio") Servizio servizio, BindingResult bindingResult,
            @RequestParam("insegnanteId") Long insegnanteId, Model model) {
        if (bindingResult.hasErrors()) {
            return mostraForm(model);
        }
        try {
            servizioService.crea(servizio, insegnanteId);
        } catch (ServizioExistsException e) {
            bindingResult.rejectValue("nome", "servizio.duplicate", e.getMessage());
            return mostraForm(model);
        }
        return "redirect:/admin/servizi";
    }

    @GetMapping("/admin/servizi/{id}/edit")
    public String modificaForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("servizio", servizioService.getServizio(id));
        return mostraForm(model);
    }

    @PostMapping("/admin/servizi/{id}")
    public String modifica(  @Valid @ModelAttribute Servizio servizio, @PathVariable("id") Long id, BindingResult bindingResult, @RequestParam("insegnanteId") Long insegnanteId, Model model) {
        if(bindingResult.hasErrors()){
            servizio.setId(id);
            return mostraForm(model);
        }
        try {
            servizioService.aggiorna(id,servizio, insegnanteId);
        } catch (ServizioExistsException e) {
            bindingResult.rejectValue("nome", "servizio.duplicate", e.getMessage());
            servizio.setId(id);
            return mostraForm(model);
        }
        return "redirect:/admin/servizi";
    }
}

